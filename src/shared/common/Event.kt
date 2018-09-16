package shared.common

import shared.normalizeEventName
import shared.setTimeout
import shared.struct.SafeEventKey


object Event {

	const val LOCAL_EVENT_PREFIX = "local"

	const val SAFE_EVENT_PREFIX = "safe"

	var clientToken: SafeEventKey? = null

//	inline fun <reified T> emit(data: T) {
//		emit(LOCAL_EVENT_PREFIX + normalizeEventName(T::class.toString()), data)
//		Console.debug("local event " + LOCAL_EVENT_PREFIX + normalizeEventName(T::class.toString()) + " sent")
//	}
//
//	inline fun <reified T> on(noinline function: (T) -> Unit) {
//		on(normalizeEventName(T::class.toString()), function)
//	}
//
//	fun <T> on(eventName: String, function: (T) -> Unit) {
//		Console.info("local event $eventName registered")
//		shared.common.on(LOCAL_EVENT_PREFIX + eventName) { data: T ->
//			Console.debug("local event $eventName triggered")
//			function(data)
//		}
//	}

	val eventsHandlers = mutableMapOf<String, MutableList<(dynamic) -> Unit>>()

	inline fun <reified T> emit(data: T) {
		val eventName = normalizeEventName(T::class.toString())

		if (!eventsHandlers.containsKey(eventName)) {
			eventsHandlers[eventName] = mutableListOf()
		}

		val eventHandlers = eventsHandlers[eventName]

		Console.debug("local event $LOCAL_EVENT_PREFIX$eventName sent")

		setTimeout {
			eventHandlers?.forEach {

				it(data)
			}
		}
	}

	inline fun <reified T> on(noinline function: (T) -> Unit) {
		val eventName = normalizeEventName(T::class.toString())

		if (!eventsHandlers.containsKey(eventName)) {
			eventsHandlers[eventName] = mutableListOf()
		}

		val eventHandlers = eventsHandlers[eventName]

		Console.info("local event $eventName registered")

		eventHandlers?.add(function)
	}
}