package client.extensions

import shared.Console
import shared.Event
import shared.Exports
import shared.normalizeEventName
import shared.struct.SafeEventContainer


fun Event.emitNet(data: Any) {
	Exports.emitNet(normalizeEventName(data::class), data)
	Console.debug("net event " + normalizeEventName(data::class) + " sent")
}

inline fun <reified T> Event.onNet(noinline function: (T) -> Unit) {
	Event.onNet(normalizeEventName(T::class), function)
}

fun <T> Event.onNet(eventName: String, function: (T) -> Unit) {
	Console.info("net event $eventName registered")

	shared.onNet(eventName) { data: T ->
		Console.debug("net event $eventName triggered")
		function(data)
	}
}

fun Event.emitSafeNet(data: Any) {
	Event.clientToken?.let { token ->
		Exports.emitNet(
				SAFE_EVENT_PREFIX + normalizeEventName(data::class),
				SafeEventContainer(token, data)
		)
		Console.debug("safe net event " + normalizeEventName(data::class) + " sent")
	}
}
