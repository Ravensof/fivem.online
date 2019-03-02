package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.Strings
import online.fivem.server.entities.Player
import online.fivem.server.entities.mysqlEntities.BlackListTable
import online.fivem.server.entities.mysqlEntities.CharacterEntity
import online.fivem.server.entities.mysqlEntities.UserEntity
import online.fivem.server.events.PlayerConnectedEvent
import online.fivem.server.extensions.row
import online.fivem.server.extensions.rowAsync
import online.fivem.server.extensions.send
import online.fivem.server.extensions.sendAsync
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.client_event_exchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class SessionModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {


	private val players = mutableMapOf<PlayerSrc, Player>()
	private val basicsModule by moduleLoader.delegate<BasicsModule>()
	private val mySQL by moduleLoader.delegate<MySQLModule>()

	override fun onInit() {
		Exports.on(NativeEvents.Server.PLAYER_CONNECTING, ::onClientConnecting)
		Exports.on(NativeEvents.Server.PLAYER_DROPPED, ::onPlayerDropped)

		ClientEvent.onGuest<ImReadyEvent> { playerSrc: PlayerSrc, _ -> onClientReady(playerSrc) }

	}

	fun getConnectedPlayers(): List<PlayerSrc> {
		return players.map { it.key }
	}

	fun getPlayer(playerSrc: Int): Player? {
		players.forEach {
			if (it.key.value == playerSrc) {
				return it.value
			}
		}

		return null
	}

	private fun onClientReady(playerSrc: PlayerSrc) = launch {

		val identifiers = Natives.getPlayerIdentifiers(playerSrc)

		val user = mySQL.connection.row<UserEntity>(
			"""SELECT id
					|FROM users
					|WHERE
					|   steam=? AND
					|   license=?
					|""".trimMargin(),
			arrayOf(
				identifiers.steam,
				identifiers.license
			)
		) ?: return@launch Natives.dropPlayer(playerSrc, Strings.NO_SUCH_USER)

		val character =
			mySQL.connection.row<CharacterEntity>(
				"""SELECT *
						|FROM characters
						|WHERE user_id=?
						|""".trimMargin(),
				arrayOf(user.id)
			) ?: return@launch Natives.dropPlayer(playerSrc, Strings.NO_SUCH_CHARACTER)

		val sessionId = mySQL.connection.send(
			"""INSERT INTO sessions
					|SET
					|  user_id=?,
					|  character_id=?,
					|  steam=?,
					|  discord=?,
					|  license=?,
					|  ip=?
					|""".trimMargin(),
			arrayOf(
				user.id,
				character.id,
				identifiers.steam,
				identifiers.discord,
				identifiers.license,
				identifiers.ip
			)
		).insertId ?: return@launch Natives.dropPlayer(playerSrc, Strings.SESSION_HAVE_NOT_BEEN_CREATED)

		val player = Player(
			playerSrc = playerSrc,
			name = identifiers.name.orEmpty(),
			sessionId = sessionId,
			characterId = character.id
		)

		players[playerSrc] = player

		basicsModule.spawn(
			player,
			CoordinatesX(
				character.coord_x.toFloat(),
				character.coord_y.toFloat(),
				character.coord_z.toFloat(),
				character.coord_rotation
			), character.pedestrian
		)

		Event.emit(PlayerConnectedEvent(player))
	}

	private fun onPlayerDropped(playerId: Int, reason: String) {

		players.forEach {
			if (it.key.value == playerId) {
				val player = it.value
				players.remove(it.key)

				mySQL.connection.sendAsync(
					"""UPDATE sessions
						|SET
						|   left_reason=?,
						|   logout_date=NOW()
						|WHERE id=?
						|LIMIT 1""".trimMargin(),
					arrayOf(
						reason,
						player.sessionId
					)
				)

				return Console.log("disconnected ${player.name}: $reason")
			}
		}

		Console.log("disconnected $playerId: $reason")
	}

	private fun onClientConnecting(source: Int, playerName: String, setKickReason: (reason: String) -> Unit) {

		val identifiers = Natives.getPlayerIdentifiers(source)

		val blackList = mySQL.connection.rowAsync<BlackListTable>(
			"""SELECT reason
				|FROM `black_list`
				|WHERE
				|   ip=? OR
				|   steam=? OR
				|   license=?
				|""".trimMargin(),
			arrayOf(
				identifiers.ip,
				identifiers.steam,
				identifiers.license
			)
		)

		Console.log("connecting $playerName ${identifiers.ip} ${identifiers.license} ${identifiers.steam}")

		launch {
			blackList.await()?.let {
				val reason = Strings.YOU_ARE_BANNED_FROM_THIS_SERVER.replace("%s", it.reason.orEmpty())
				Natives.mainThread {
					setKickReason(reason)
				}
			}
		}
	}
}