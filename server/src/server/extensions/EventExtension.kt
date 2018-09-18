package server.extensions

import server.common.Server
import server.common.getPlayersIds
import server.structs.PlayerSrc
import shared.common.Console
import shared.common.Event
import shared.common.Exports
import shared.entities.Player
import shared.normalizeEventName
import shared.struct.SafeEventContainer

fun Event.emitNet(player: Player, data: Any) {
	shared.common.emitNet(normalizeEventName(data::class.toString()), player.id.toString(), data)
	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent to " + player.id)
}

fun Event.emitNetAll(data: Any) {
	val eventName = normalizeEventName(data::class.toString())

	Server.getPlayersIds().forEach {
		shared.common.emitNet(eventName, it.toString(), data)
	}

	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent to all")
}

inline fun <reified T> Event.onNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onNet(normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	Console.info("net event $eventName registered")

	Exports.onNet(eventName) { playerId: Int, data: T, numberOfPlayers: Int ->

		val playerSrc = PlayerSrc(playerId)

		Console.debug("numberOfPlayers: $numberOfPlayers")

//		if (Server.getNumPlayersOnline() - numberOfPlayers < 4) {
		Console.debug("net event $eventName triggered for $playerSrc")
			function(playerSrc, data)
//		} else {
//			console.log("net event $eventName rejected for $playerSrc")
//		}
	}
}

inline fun <reified T> Event.onSafeNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onSafeNet(Event.SAFE_EVENT_PREFIX + normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onSafeNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	console.log("safe net event $eventName registered")

	shared.common.onNet(eventName) { playerId: Int, safeEventContainer: SafeEventContainer<T>, numberOfPlayers: Int ->

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
