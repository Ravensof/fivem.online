package server.extensions

import server.Server
import server.getNumPlayersOnline
import server.structs.PlayerSrc
import shared.Event
import shared.entities.Player
import shared.exports
import shared.normalizeEventName
import shared.r.MODULE_FOLDER_NAME

fun Event.emitNet(player: Player, data: Any) {
	shared.emitNet(normalizeEventName(data::class.toString()), player.id.toString(), data)
	console.log("net event " + normalizeEventName(data::class.toString()) + " sent to " + player.id)
}

fun Event.emitNetAll(data: Any) {
	shared.emitNet(normalizeEventName(data::class.toString()), data)
	console.log("net event " + normalizeEventName(data::class.toString()) + " sent to all")
}

inline fun <reified T> Event.onNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onNet(normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	console.log("net event $eventName registered")

	exports[MODULE_FOLDER_NAME].onNet(eventName) { playerId: Int, data: T, numberOfPlayers: Int ->

		val playerSrc = PlayerSrc(playerId)

		if (Server.getNumPlayersOnline() - numberOfPlayers < 4) {
			console.log("net event $eventName triggered for $playerSrc")
			function(playerSrc, data)
		} else {
			console.log("net event $eventName rejected for $playerSrc")
		}
	}
}

//inline fun <reified T> Event.onNet(noinline function: (Player, T) -> Unit) {
//	Event.onNet(T::class.toString(), function)
//}
//
//fun <T> Event.onNet(eventName: String, function: (Player, T) -> Unit) {
//	console.log("net event $eventName registered")
//
//	shared.onNet(eventName) { player: Player, data: T ->
//		if (PlayersManager.isPlayerRegistered(player)) {
//			console.log("net event " + eventName + " triggered for " + player.id)
//			function(player, data)
//		} else {
//			console.log("net event " + eventName + " rejected for " + player.id)
//		}
//	}
//}
