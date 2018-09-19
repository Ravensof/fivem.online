package server.modules.session.extensions

import server.common.Server
import server.modules.session.SessionModule
import server.structs.PlayerSrc
import shared.common.Console
import shared.common.Event
import shared.common.Exports
import shared.normalizeEventName

inline fun <reified T> Event.onSafeNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onSafeNet(Event.SAFE_EVENT_PREFIX + normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onSafeNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	Console.info("safe net event $eventName registered")

	Exports.onNet(eventName) { playerId: Int, numberOfPlayers: Int, token: String, data: T ->

		val playerSrc = PlayerSrc(playerId)

		Console.checkValue("numberOfPlayers", "client: $numberOfPlayers != server: ${Server.getNumPlayersOnline()}") { numberOfPlayers != Server.getNumPlayersOnline() }

		if (SessionModule.getInstance().checkToken(playerSrc, token)) {
			Console.debug("safe net event $eventName triggered for ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
			function(playerSrc, data)
		} else {
			Console.warn("safe net event $eventName rejected for ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
		}
	}
}