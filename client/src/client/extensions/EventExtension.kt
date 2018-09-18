package client.extensions

import client.common.Client
import shared.common.Console
import shared.common.Event
import shared.common.Exports
import shared.normalizeEventName


fun Event.emitNet(data: Any) {
	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent")
	Exports.emitNet(normalizeEventName(data::class.toString()), data)
}

inline fun <reified T> Event.onNet(noinline function: (T) -> Unit) {
	Event.onNet(normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onNet(eventName: String, function: (T) -> Unit) {
	Console.info("net event $eventName registered")

	shared.common.onNet(eventName) { data: T ->
		Console.debug("net event $eventName triggered")
		function(data)
	}
}

//fun Event.emitSafeNet(data: Any) {
//	Event.clientToken?.let { token ->
//		Console.debug("safe net event " + normalizeEventName(data::class.toString()) + " sent")
//		Exports.emitNet(
//				SAFE_EVENT_PREFIX + normalizeEventName(data::class.toString()),
//				SafeEventContainer(token, data)
//		)
//		Unit
//	}
//}

fun Event.onNui(eventName: String, function: (Any, (String) -> Unit) -> Unit) {
	Console.info("nui event $eventName registered")
	Exports.onNui(eventName) { data: Any, callback: (String) -> Unit ->
		Console.debug("nui event $eventName triggered")
		function(data, callback)
	}
}

fun Event.emitNui(obj: Any): Int {
	Console.debug("nui data sent " + obj::class.toString() + " " + JSON.stringify(obj))
	return Client.sendNuiMessage(obj)
}
