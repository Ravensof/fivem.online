package shared


object Event {

	private const val LOCAL_EVENT_PREFIX = "local"

	fun emit(data: Any) {
		emit(LOCAL_EVENT_PREFIX + normalizeEventName(data::class.toString()), data)
		console.log("local event " + LOCAL_EVENT_PREFIX + normalizeEventName(data::class.toString()) + " sent")
	}

	inline fun <reified T> on(noinline function: (T) -> Unit) {
		on(normalizeEventName(T::class.toString()), function)
	}

	fun <T> on(eventName: String, function: (T) -> Unit) {
		console.log("local event $eventName registered")
		shared.on(LOCAL_EVENT_PREFIX + eventName) { data: T ->
			console.log("local event $eventName triggered")
			function(data)
		}
	}
}