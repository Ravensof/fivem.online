package server.modules.session

import fivem.common.Exports
import server.common.MySQL
import server.common.Server
import server.extensions.emitNet
import server.extensions.onNet
import server.modules.AbstractModule
import server.modules.session.extensions.onSafeNet
import server.structs.PlayerSrc
import server.structs.tables.ConnectionLogTable
import universal.common.Console
import universal.common.Event
import universal.common.setTimeout
import universal.events.ClientReady
import universal.extensions.onNull
import universal.r.NativeEvents
import kotlin.js.Date
import kotlin.random.Random

class SessionModule private constructor() : AbstractModule() {

	private val playersIds = mutableMapOf<Int, String>()

	init {
		Exports.on(NativeEvents.Server.PLAYER_CONNECTING, ::onClientConnecting)

		Exports.on(NativeEvents.Server.PLAYER_DROPPED, ::onPlayerDropped)

		Event.onNet<ClientReady> { playerSrc: PlayerSrc, _ ->
			onClientReady(playerSrc)
		}

		Event.onSafeNet<String> { playerSrc: PlayerSrc, string ->
			Console.debug(string)
		}

		Server.getPlayersIds().forEach {
			val playerSrc = PlayerSrc(it)

			onClientConnecting(it, Server.getPlayerName(playerSrc).orEmpty()) {
				Server.dropPlayer(playerSrc, it)
			}
		}

		Console.info("SessionModule loaded")
	}

	fun checkToken(playerSrc: PlayerSrc, token: String): Boolean {
		return playersIds.containsKey(playerSrc.value) && playersIds.containsValue(token)
	}

	private fun onPlayerDropped(playerId: Int, reason: String) {
		playersIds.remove(playerId)
	}

	private fun onClientConnecting(source: Int, playerName: String, setKickReason: (reason: String) -> Unit) {
		setTimeout {
			val playerSrc = PlayerSrc(source)

			saveConnectionData(playerSrc, playerName)
		}
//DropPlayer(playerSrc, Strings.SERVER_REGISTER_PLAYER_ERROR)
//		setKickReason("test for $playerName with id = $source and "+Server.getPlayerEndpoint(PlayerSrc(source)))
//		Server.cancelEvent()
	}

	private fun onClientReady(playerSrc: PlayerSrc) {
		if (playersIds.contains(playerSrc.value)) {
			Console.warn("trying accessing to player ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
			return
		}

		val token = generateToken()

		playersIds.put(playerSrc.value, token)

		Event.emitNet(playerSrc, ClientReady(token))
	}

	private fun saveConnectionData(playerSrc: PlayerSrc, playerName: String) {
		val playerIdentifiers = Server.getPlayerIdentifiers(playerSrc)

		MySQL.execute("INSERT INTO ${ConnectionLogTable.TABLE_NAME} " +
				"SET ${ConnectionLogTable.FIELD_LAST_NAME}=${MySQL.filter(playerName)}, " +
				"${ConnectionLogTable.FIELD_TIME}=${Date().getTime()}," +
				"${ConnectionLogTable.FIELD_STEAM}=${MySQL.filter(playerIdentifiers.steam.orEmpty())}, " +
				"${ConnectionLogTable.FIELD_LICENSE}=${MySQL.filter(playerIdentifiers.license.orEmpty())}, " +
				"${ConnectionLogTable.FIELD_IP}=${MySQL.filter(playerIdentifiers.ip.orEmpty())} " +
				"ON DUPLICATE KEY UPDATE " +
				"${ConnectionLogTable.FIELD_LAST_NAME}=${MySQL.filter(playerName)}," +
				"${ConnectionLogTable.FIELD_TIME}=${Date().getTime()}")
	}

	private fun isTokenExists(token: String): Boolean {
		return playersIds.containsValue(token)
	}

	private fun generateToken(): String {
		var token = Random.nextBytes(TOKEN_SIZE).toString()

		while (isTokenExists(token)) {
			token = Random.nextBytes(TOKEN_SIZE).toString()
		}

		return token
	}


	companion object {
		const val TOKEN_SIZE = 10

		private var instance: SessionModule? = null

		fun getInstance(): SessionModule {
			instance.onNull {
				instance = SessionModule()
			}

			return instance!!
		}
	}
}