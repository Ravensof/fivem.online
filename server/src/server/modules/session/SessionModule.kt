package server.modules.session

import server.common.Server
import server.extensions.emitNet
import server.extensions.onNet
import server.modules.AbstractModule
import server.modules.session.extensions.onSafeNet
import server.structs.PlayerSrc
import shared.common.Console
import shared.common.Event
import shared.common.Exports
import shared.events.ClientReady
import shared.extensions.onNull
import shared.r.NativeEvents
import kotlin.random.Random

class SessionModule private constructor() : AbstractModule() {

	private val playersIds = mutableMapOf<Int, String>()

	init {
		Event.onNet<ClientReady> { playerSrc: PlayerSrc, _ ->
			onClientReady(playerSrc)
		}

		Exports.on(NativeEvents.Server.PLAYER_DROPPED, ::onPlayerDropped)

		Event.onSafeNet<String> { playerSrc: PlayerSrc, string ->
			Console.debug(string)
		}

	}

	fun checkToken(playerSrc: PlayerSrc, token: String): Boolean {
		return playersIds.containsKey(playerSrc.value) && playersIds.containsValue(token)
	}

	private fun onPlayerDropped(playerId: Int, reason: String) {
		playersIds.remove(playerId)
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