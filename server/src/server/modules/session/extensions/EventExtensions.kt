package server.modules.session.extensions

import fivem.common.Exports
import server.common.Server
import server.modules.session.SessionModule
import server.structs.PlayerSrc
import universal.common.Console
import universal.common.Event
import universal.common.normalizeEventName
import universal.r.Strings

inline fun <reified T> Event.onSafeNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onSafeNet(Event.SAFE_EVENT_PREFIX + normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onSafeNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	Console.info("safe net event $eventName registered")

	Exports.onNet(eventName) { playerId: Int, numberOfPlayers: Int, token: String, data: T ->

		val playerSrc = PlayerSrc(playerId)
		val serverNumberOfPlayers = Server.getNumPlayersOnline()

		when {
			numberOfPlayers == 1 && serverNumberOfPlayers > 1 -> {
				Console.log("net event $eventName rejected for ${Server.getPlayerName(playerSrc)} (${playerSrc.value}) solo session")
				Server.dropPlayer(playerSrc, Strings.CLIENT_IN_SINGLE_SESSION)
			}
			!SessionModule.getInstance().checkToken(playerSrc, token) -> {
				Console.warn("safe net event $eventName rejected for ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
			}

			else -> {
				Console.debug("safe net event $eventName triggered for ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
				function(playerSrc, data)
			}
		}
	}
}