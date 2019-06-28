package online.fivem.server.modules.roleplay_system

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.events.net.SpawnVehiclesCommand
import online.fivem.common.events.net.VehiclesSpawnedEvent
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.entities.Player
import online.fivem.server.entities.mysqlEntities.VehiclesEntity
import online.fivem.server.events.PlayerConnectedEvent
import online.fivem.server.events.PlayerDisconnectedEvent
import online.fivem.server.modules.basics.mysql.MySQLModule
import online.fivem.server.modules.basics.mysql.extensions.fetch
import online.fivem.server.modules.basics.mysql.extensions.getConnection
import online.fivem.server.modules.basics.mysql.extensions.send
import online.fivem.server.modules.client_event_exchanger.ClientEvent

class VehiclesSyncModule(
	private val mySQLModule: MySQLModule
) : AbstractServerModule() {

	private var playersCount = 0

	override fun onStartAsync() = async {
		mySQLModule.waitForStart()

		Event.on<PlayerConnectedEvent> { onPlayerConnected(it.player) }
		Event.on<PlayerDisconnectedEvent> { onPlayerDisconnected(it.player) }
	}

	override fun onSync(player: Player, data: ClientSideSynchronizationEvent) = launch {

		val event = data.vehiclesSyncClientEvent ?: return@launch

		val connection = mySQLModule.pool.getConnection()

		event.vehicles.forEach {
			//language=sql
			connection.send(
				"""UPDATE vehicles
					SET
						coord_x=?,
						coord_y=?,
						coord_z=?,
						coord_rotation=?
					WHERE network_id=?
					LIMIT 1
		""".trimIndent(),
				arrayOf(
					it.coordinatesX.x,
					it.coordinatesX.y,
					it.coordinatesX.z,
					it.coordinatesX.rotation,

					it.networkId
				)
			)
		}
	}

	override fun onSync(exportObject: ServerSideSynchronizationEvent): Job? {

		return null
	}

	private fun onPlayerConnected(player: Player) = launch {
		playersCount++

		if (playersCount != 1) return@launch//todo unsafe

		val connection = mySQLModule.pool.getConnection()

		//		language=sql
		val vehicles = connection.fetch<VehiclesEntity>(
			"SELECT * FROM vehicles WHERE location_id=0"
		).map {
			SpawnVehiclesCommand.Vehicle(
				id = it.id,
				modelHash = it.model_hash,
				coordinatesX = CoordinatesX(
					x = it.coord_x.toFloat(),
					y = it.coord_y.toFloat(),
					z = it.coord_z.toFloat(),
					rotation = it.coord_rotation
				)
			)
		}

		val channel = ClientEvent.openSubscription(VehiclesSpawnedEvent::class)

		ClientEvent.emit(
			SpawnVehiclesCommand(
				vehicles = vehicles
			),
			player
		)

		withTimeout(20_000) {
			val response = channel.receiveAndCancel()

			if (response.player != player) return@withTimeout player.drop("you are not supposed to be here")

			response.data.ids.forEach {
				//language=sql
				connection.send("UPDATE vehicles SET network_id=? WHERE id=? LIMIT 1", arrayOf(it.value, it.key))
			}
		}
	}

	private fun onPlayerDisconnected(player: Player) {
		playersCount--
	}
}