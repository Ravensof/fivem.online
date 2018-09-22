package client.extensions

import client.common.Client
import fivem.common.Exports
import universal.common.Console
import universal.common.Event
import universal.common.normalizeEventName


fun Event.emitNet(data: Any) {
	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent")
	Exports.emitNet(normalizeEventName(data::class.toString()), data)
}

inline fun <reified T> Event.onNet(noinline function: (T) -> Unit) {
	Event.onNet(normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onNet(eventName: String, function: (T) -> Unit) {
	Console.info("net event $eventName registered")

	fivem.common.onNet(eventName) { data: T ->
		Console.debug("net event $eventName triggered")
		function(data)
	}
}

inline fun <reified T : Any> Event.onNui(noinline function: (T, (String) -> Unit) -> Unit) {
	Event.onNui(normalizeEventName(T::class.toString()), function)
}

fun <T> Event.onNui(eventName: String, function: (T, (String) -> Unit) -> Unit) {
	Console.info("nui event $eventName registered")
	Exports.onNui(eventName) { data: T, callback: (String) -> Unit ->
		Console.debug("nui event $eventName triggered")
		function(data, callback)
	}
}

fun Event.emitNui(data: Any): Int {
	val eventName = normalizeEventName(data::class.toString())

	Console.debug("nui data sent " + eventName + " " + JSON.stringify(data))

	return Client.sendNuiMessage(object {
		val event = eventName
		val data = data
	})
}

//fun Event.emitNui(obj: Any): Int {
//	Console.debug("nui data sent " + obj::class.toString() + " " + JSON.stringify(obj))
//	return Client.sendNuiMessage(obj)
//}
