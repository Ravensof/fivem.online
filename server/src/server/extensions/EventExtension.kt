package server.extensions

import server.structs.PlayerSrc
import shared.Console
import shared.Event
import shared.Exports
import shared.entities.Player
import shared.normalizeEventName
import shared.struct.SafeEventContainer

fun Event.emitNet(player: Player, data: Any) {
	shared.emitNet(normalizeEventName(data::class), player.id.toString(), data)
	console.log("net event " + normalizeEventName(data::class) + " sent to " + player.id)
}

fun Event.emitNetAll(data: Any) {
	shared.emitNet(normalizeEventName(data::class), data)
	console.log("net event " + normalizeEventName(data::class) + " sent to all")
}

inline fun <reified T> Event.onNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onNet(normalizeEventName(T::class), function)
}

fun <T> Event.onNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	console.log("net event $eventName registered")

	Exports.onNet(eventName) { playerId: Int, data: T, numberOfPlayers: Int ->

		val playerSrc = PlayerSrc(playerId)

		Console.debug("numberOfPlayers: $numberOfPlayers")

//		if (Server.getNumPlayersOnline() - numberOfPlayers < 4) {
			console.log("net event $eventName triggered for $playerSrc")
			function(playerSrc, data)
//		} else {
//			console.log("net event $eventName rejected for $playerSrc")
//		}
	}
}

inline fun <reified T> Event.onSafeNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onSafeNet(Event.SAFE_EVENT_PREFIX + normalizeEventName(T::class), function)
}

fun <T> Event.onSafeNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	console.log("safe net event $eventName registered")

	shared.onNet(eventName) { playerId: Int, safeEventContainer: SafeEventContainer<T>, numberOfPlayers: Int ->

		val playerSrc = PlayerSrc(playerId)

		Console.debug("numberOfPlayers: $numberOfPlayers")

//		if (Server.getNumPlayersOnline() - numberOfPlayers < 4) {//todo проверка токена
		console.log("net event $eventName triggered for $playerSrc")
		function(playerSrc, safeEventContainer.data)
//		} else {
//			console.log("net event $eventName rejected for $playerSrc")
//		}
	}
}
