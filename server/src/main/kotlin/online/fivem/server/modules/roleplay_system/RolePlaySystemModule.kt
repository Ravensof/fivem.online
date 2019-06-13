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
import online.fivem.server.entities.mysqlEntities.CharacterWeaponsEntity
import online.fivem.server.events.PlayerConnectedEvent
import online.fivem.server.modules.basics.mysql.MySQLModule
import online.fivem.server.modules.basics.mysql.extensions.fetch
import online.fivem.server.modules.basics.mysql.extensions.getConnection
import online.fivem.server.modules.basics.mysql.extensions.row
import online.fivem.server.modules.basics.mysql.extensions.send
import online.fivem.server.modules.client_event_exchanger.ClientEvent

class RolePlaySystemModule(
	private val mySQLModule: MySQLModule
) : AbstractServerModule() {

	private lateinit var mySQL: Pool

	override suspend fun onInit() {
		moduleLoader.add(VehiclesSyncModule(mySQLModule))
	}

	override fun onStart() = launch {
		mySQLModule.waitForStart()
		mySQL = mySQLModule.pool

		Event.on<PlayerConnectedEvent> { onPlayerConnected(it.player) }
	}

	override fun onSync(player: Player, data: ClientSideSynchronizationEvent) = launch {
		val event = data.rolePlaySystem ?: return@launch

		val connection = mySQL.getConnection()

		//language=sql
		connection.send(
			"""UPDATE characters
				SET
					coord_x=?,
					coord_y=?,
					coord_z=?,
					coord_rotation=?,
					health=?,
					armour=?

				WHERE id=?
				LIMIT 1
				""".trimIndent(),
			arrayOf(
				event.coordinatesX.x,
				event.coordinatesX.y,
				event.coordinatesX.z,
				event.coordinatesX.rotation,
				event.health,
				event.armour,

				player.characterId
			)
		)

		//language=sql
		connection.send(
			"""DELETE
				FROM character_weapons
				WHERE character_id=?
		""".trimIndent(),
			player.characterId
		)

		event.weapons.forEach {
			//language=sql
			connection.send(
				"""INSERT INTO character_weapons
					SET
						character_id=?,
						weapon_id=?,
						count=?
						""".trimIndent(),
				arrayOf(
					player.characterId,
					it.key,
					it.value
				)
			)
		}
	}

	private fun onPlayerConnected(player: Player) = launch {

		val connection = mySQL.getConnection()

		//language=sql
		val character =
			connection.row<CharacterEntity>(
				"""SELECT *
					FROM characters
					WHERE user_id=?
					""".trimIndent(),
				arrayOf(player.userId)
			) ?: return@launch player.drop(Strings.NO_SUCH_CHARACTER)

		player.characterId = character.id

		//language=sql
		val weapons = connection.fetch<CharacterWeaponsEntity>(
			"""SELECT
				weapon_id,
				count
				FROM character_weapons
				WHERE character_id=?
				""".trimIndent(),
			character.id
		)

		ClientEvent.emit(
			SpawnPlayerEvent(
				coordinatesX = CoordinatesX(
					character.coord_x.toFloat(),
					character.coord_y.toFloat(),
					character.coord_z.toFloat(),
					character.coord_rotation
				),

				pedModel = character.pedestrian,

				health = character.health,

				armour = character.armour,

				weapons = weapons.associate { it.weapon_id to it.count }
			), player
		)
	}
}