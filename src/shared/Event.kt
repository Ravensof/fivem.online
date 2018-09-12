package shared

import shared.struct.SafeEventKey


object Event {

	private const val LOCAL_EVENT_PREFIX = "local"

	const val SAFE_EVENT_PREFIX = "safe"

	var clientToken: SafeEventKey? = null

	fun emit(data: Any) {
		emit(LOCAL_EVENT_PREFIX + normalizeEventName(data::class), data)
		Console.debug("local event " + LOCAL_EVENT_PREFIX + normalizeEventName(data::class) + " sent")
	}

	inline fun <reified T> on(noinline function: (T) -> Unit) {
		on(normalizeEventName(T::class), function)
	}

	fun <T> on(eventName: String, function: (T) -> Unit) {
		Console.info("local event $eventName registered")
		shared.on(LOCAL_EVENT_PREFIX + eventName) { data: T ->
			Console.debug("local event $eventName triggered")
			function(data)
		}
	}
}