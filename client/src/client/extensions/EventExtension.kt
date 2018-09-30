package client.extensions

import DEBUG_NUI
import client.common.Client
import fivem.common.Exports
import universal.common.Console
import universal.common.Event
import universal.common.normalizeEventName
import universal.events.IEvent

@Deprecated("use Event.emitSafeNet(data) instead")
inline fun <reified T : IEvent> Event.emitNet(data: T) {
	Console.debug("net event " + normalizeEventName(data::class.toString()) + " sent")
	Exports.emitNet(normalizeEventName(data::class.toString()), data.serialize())
}

inline fun <reified T : IEvent> Event.onNet(noinline function: (T) -> Unit) {
	Event.onNet(normalizeEventName(T::class.toString()), function)
}

fun <T : IEvent> Event.onNet(eventName: String, function: (T) -> Unit) {
	Console.info("net event $eventName registered")

	fivem.common.onNet(eventName) { data: T ->
		Console.debug("net event $eventName triggered")
		function(IEvent.unserialize(data))
	}
}

inline fun <reified T : IEvent> Event.onNui(noinline function: (T, (String) -> Unit) -> Unit) {
	Event.onNui(normalizeEventName(T::class.toString()), function)
}

fun <T : IEvent> Event.onNui(eventName: String, function: (T, (String) -> Unit) -> Unit) {
	Console.info("nui event $eventName registered")
	Exports.onNui(eventName) { data: T, callback: (String) -> Unit ->
		if (DEBUG_NUI) {
			Console.debug("nui event $eventName triggered")
		}
		function(IEvent.unserialize(data), callback)
	}
}

inline fun <reified T : IEvent> Event.emitNui(data: T): Int {
	val eventName = normalizeEventName(data::class.toString())

	if (DEBUG_NUI) {
		Console.debug("nui data sent " + eventName + " " + JSON.stringify(data))
	}

	return Client.sendNuiMessage(object {
		val event = eventName
		val data = data.serialize()
	})
}
