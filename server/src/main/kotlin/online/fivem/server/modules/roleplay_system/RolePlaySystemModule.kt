package online.fivem.server.modules.roleplay_system

import external.nodejs.mysql.Pool
import kotlinx.coroutines.launch
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.server.Strings
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.entities.Player
import online.fivem.server.entities.mysqlEntities.CharacterEntity
import online.fivem.server.events.PlayerConnectedEvent
import online.fivem.server.extensions.getConnection
import online.fivem.server.extensions.row
import online.fivem.server.modules.basics.mysql.MySQLModule
import online.fivem.server.modules.client_event_exchanger.ClientEvent

class RolePlaySystemModule : AbstractServerModule() {

	private lateinit var mySQL: Pool

	override fun onInit() {
		Event.on<PlayerConnectedEvent> { onPlayerConnected(it.player) }
	}

	override fun onStart() = launch {
		mySQL = moduleLoader.getModule(MySQLModule::class).pool
	}

	private suspend fun spawn(player: Player, coordinatesX: CoordinatesX, pedHash: Int) {
		ClientEvent.emit(SpawnPlayerEvent(coordinatesX, pedHash), player)
	}

	private suspend fun onPlayerConnected(player: Player) {

		val character =
			mySQL.getConnection().row<CharacterEntity>(
				"""SELECT *
						|FROM characters
						|WHERE user_id=?
						|""".trimMargin(),
				arrayOf(player.userId)
			) ?: return player.drop(Strings.NO_SUCH_CHARACTER)

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