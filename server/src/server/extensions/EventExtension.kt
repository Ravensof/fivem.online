package server.extensions

import fivem.common.Exports
import server.common.Server
import server.structs.PlayerSrc
import universal.common.Console
import universal.common.Event
import universal.common.normalizeEventName
import universal.entities.Player

fun Event.emitNet(player: Player, data: Any) {
	fivem.common.emitNet(normalizeEventName(data::class.toString()), player.id.toString(), data)
	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent to " + player.id)
}

fun Event.emitNet(playerSrc: PlayerSrc, data: Any) {
	fivem.common.emitNet(normalizeEventName(data::class.toString()), playerSrc.value.toString(), data)
	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent to ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
}

fun Event.emitNetAll(data: Any) {
	val eventName = normalizeEventName(data::class.toString())

	Server.getPlayersIds().forEach {
		fivem.common.emitNet(eventName, it.toString(), data)
	}

	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent to all")
}

inline fun <reified T> Event.onNet(noinline function: (PlayerSrc, T) -> Unit) {
	Event.onNet(normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onNet(eventName: String, function: (PlayerSrc, T) -> Unit) {
	Console.info("net event $eventName registered")

	Exports.onNet(eventName) { playerId: Int, numberOfPlayers: Int, data: T ->

		val playerSrc = PlayerSrc(playerId)

		Console.checkValue("numberOfPlayers", "client: $numberOfPlayers != server: ${Server.getNumPlayersOnline()}") { numberOfPlayers != Server.getNumPlayersOnline() }

//		if (Server.getNumPlayersOnline() - numberOfPlayers < 4) {
		Console.debug("net event $eventName triggered for ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
			function(playerSrc, data)
//		} else {
//			console.log("net event $eventName rejected for $playerSrc")
//		}
	}
}

