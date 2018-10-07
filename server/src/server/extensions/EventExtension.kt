package server.extensions

import fivem.common.Exports
import server.common.Server
import server.structs.PlayerSrc
import universal.common.Console
import universal.common.Event
import universal.common.Serializable
import universal.common.normalizeEventName
import universal.events.IEvent
import universal.r.Strings

//fun Event.emitNet(player: Player, data: Any) {
//	fivem.common.emitNet(normalizeEventName(data::class.toString()), player.id.toString(), data)
//	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent to " + player.id)
//}

inline fun <reified T : IEvent> Event.emitNet(playerSrc: PlayerSrc, data: T) {
	fivem.common.emitNet(normalizeEventName(data::class.toString()), playerSrc.value.toString(), Serializable.prepare(data))
	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent to ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
}

inline fun <reified T : IEvent> Event.emitNetAll(data: T) {
	val eventName = normalizeEventName(data::class.toString())

	Server.getPlayersIds().forEach {
		fivem.common.emitNet(eventName, it.toString(), Serializable.prepare(data))
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
		val serverNumberOfPlayers = Server.getNumPlayersOnline()

		if (numberOfPlayers == 1 && serverNumberOfPlayers > 1) {
			Console.log("net event $eventName rejected for ${Server.getPlayerName(playerSrc)} (${playerSrc.value}) solo session")
			Server.dropPlayer(playerSrc, Strings.CLIENT_IN_SINGLE_SESSION)
		} else {
			Console.debug("net event $eventName triggered for ${Server.getPlayerName(playerSrc)} (${playerSrc.value})")
			function(playerSrc, data)
		}
	}
}

