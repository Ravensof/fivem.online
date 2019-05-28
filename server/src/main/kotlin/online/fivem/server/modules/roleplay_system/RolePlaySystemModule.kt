package online.fivem.server.modules.roleplay_system

import external.nodejs.mysql.Pool
import kotlinx.coroutines.launch
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.server.Strings
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.entities.Player
import online.fivem.server.entities.mysqlEntities.CharacterEntity
import online.fivem.server.events.PlayerConnectedEvent
import online.fivem.server.modules.basics.mysql.MySQLModule
import online.fivem.server.modules.basics.mysql.extensions.getConnection
import online.fivem.server.modules.basics.mysql.extensions.row
import online.fivem.server.modules.basics.mysql.extensions.send
import online.fivem.server.modules.client_event_exchanger.ClientEvent

class RolePlaySystemModule : AbstractServerModule() {

	private lateinit var mySQL: Pool

	override fun onStart() = launch {
		mySQL = moduleLoader.getModule(MySQLModule::class).pool

		Event.on<PlayerConnectedEvent> { onPlayerConnected(it.player) }
	}

	override fun onSync(player: Player, data: ClientSideSynchronizationEvent) = launch {
		val event = data.rolePlaySystem ?: return@launch

		event.coordinatesX.let {
			mySQL.getConnection().send(
				"""UPDATE characters
							|SET
							|   coord_x=?,
							|   coord_y=?,
							|   coord_z=?,
							|   coord_rotation=?
							|WHERE id=?
							|LIMIT 1
						""".trimMargin(),
				arrayOf(
					it.x,
					it.y,
					it.z,
					it.rotation,
					player.characterId
				)
			)
		}
	}

	private suspend fun spawn(player: Player, coordinatesX: CoordinatesX, pedHash: Int) {
		ClientEvent.emit(SpawnPlayerEvent(coordinatesX, pedHash), player)
	}

	private fun onPlayerConnected(player: Player) = launch {

		val character =
			mySQL.getConnection().row<CharacterEntity>(
				"""SELECT *
						|FROM characters
						|WHERE user_id=?
						|""".trimMargin(),
				arrayOf(player.userId)
			) ?: return@launch player.drop(Strings.NO_SUCH_CHARACTER)

		player.characterId = character.id

		spawn(
			player,
			CoordinatesX(
				character.coord_x.toFloat(),
				character.coord_y.toFloat(),
				character.coord_z.toFloat(),
				character.coord_rotation
			),
			character.pedestrian
		)
	}
}