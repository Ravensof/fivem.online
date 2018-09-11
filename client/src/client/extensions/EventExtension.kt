package client.extensions

import shared.Event
import shared.Exports
import shared.normalizeEventName


fun Event.emitNet(data: Any) {
	Exports.emitNet(normalizeEventName(data::class.toString()), data)
	console.log("net event " + normalizeEventName(data::class.toString()) + " sent")
}

//fun Event.emitNet(player: Player, data: Any) {
//	shared.emitNet(data::class.toString(), player, data)
//	console.log("net event " + data::class.toString() + " sent")
//}

inline fun <reified T> Event.onNet(noinline function: (T) -> Unit) {
	Event.onNet(normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onNet(eventName: String, function: (T) -> Unit) {
	console.log("net event $eventName registered")

	shared.onNet(eventName) { data: T ->
		console.log("net event $eventName triggered")
		function(data)
	}
}
