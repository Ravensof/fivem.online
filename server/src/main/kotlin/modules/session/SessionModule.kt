package online.fivem.server.modules.session

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.ImReadyEvent
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.Strings
import online.fivem.server.Strings.UNKNOWN_ERROR
import online.fivem.server.common.MySQL
import online.fivem.server.entities.Player
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.clientEventExchanger.ClientEvent
import online.fivem.server.mysqlEntities.CharacterEntity
import online.fivem.server.mysqlEntities.UserEntity
import online.fivem.server.mysqlTables.BlackListTable

class SessionModule : AbstractModule() {

	private val mySQL = MySQL.instance
	private val players = mutableMapOf<PlayerSrc, Player>()

	override fun init() {
		Exports.on(NativeEvents.Server.PLAYER_CONNECTING, ::onClientConnecting)
		Exports.on(NativeEvents.Server.PLAYER_DROPPED, ::onPlayerDropped)

		ClientEvent.on<ImReadyEvent> { playerSrc, _ -> onClientReady(playerSrc) }

		moduleLoader.add(SynchronizationModule())
	}

	fun getConnectedPlayers(): List<PlayerSrc> {
		return Natives.getPlayers()
	}

	private fun onClientReady(playerSrc: PlayerSrc) {
		GlobalScope.launch {

			val identifiers = Natives.getPlayerIdentifiers(playerSrc)
			val player = Player(
				playerSrc = playerSrc,
				name = identifiers.name.orEmpty()
			)

			val user = mySQL.query<UserEntity>(
				"""SELECT id
					|FROM users
					|WHERE
					|   steam=? AND
					|   license=?
					|LIMIT 1""".trimMargin(),
				identifiers.steam,
				identifiers.license
			).await().firstOrNull() ?: return@launch Natives.dropPlayer(playerSrc, "пользователь не создан")

			val character =
				mySQL.query<CharacterEntity>("SELECT id FROM characters WHERE user_id=${user.id} LIMIT 1").await().firstOrNull()
					?: return@launch Natives.dropPlayer(playerSrc, "пользователь не создан")

			val sessionId = mySQL.query(
				"""
				|INSERT INTO sessions
				|SET
				|  user_id=?,
				|  character_id=?,
				|  steam=?,
				|  license=?,
				|  ip=?
				|""".trimMargin(),
				user.id,
				character.id,
				identifiers.steam.orEmpty(),
				identifiers.license,
				identifiers.ip
			).await() ?: return@launch Natives.dropPlayer(playerSrc, UNKNOWN_ERROR)

			player.sessionId = sessionId

			players[playerSrc] = player
		}
	}

	private fun onPlayerDropped(playerId: Int, reason: String) {

		players.forEach {
			if (it.key.value == playerId) {
				val player = it.value
				players.remove(it.key)

				mySQL.send(
					"""UPDATE sessions
					|SET
					| left_reason=?,
					| logout_date=NOW()
					|WHERE id=?
					|LIMIT 1""".trimMargin(),
					reason,
					player.sessionId
				)

				return Console.log("disconnected ${player.name}: $reason")
			}
		}

		Console.log("disconnected $playerId: $reason")
	}

	private fun onClientConnecting(source: Int, playerName: String, setKickReason: (reason: String) -> Unit) {

		val identifiers = Natives.getPlayerIdentifiers(source)

		val blackList = mySQL.query<BlackListTable>(
			"""SELECT reason
				|FROM `black_list`
				|WHERE
				|   ip=? OR
				|   steam=? OR
				|   license=?
				|LIMIT 1""".trimMargin(),
			identifiers.ip,
			identifiers.steam,
			identifiers.license
		)

		Console.log("connecting $playerName ${identifiers.ip} ${identifiers.license} ${identifiers.steam}")

		GlobalScope.launch {
			val result = blackList.await()

			if (result.isNotEmpty()) {
				val reason = Strings.YOU_ARE_BANNED_FROM_THIS_SERVER.replace("%s", result.first().reason.orEmpty())
				return@launch Natives.dropPlayer(source, reason)
			}
		}
	}
}