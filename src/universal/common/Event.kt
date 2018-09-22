package universal.common

object Event {

	private const val LOCAL_EVENT_PREFIX = "local"

	const val SAFE_EVENT_PREFIX = "safe"

	private val eventsHandlers = mutableMapOf<String, MutableList<(dynamic) -> Unit>>()
	private val eventsHandlersRegistry = mutableListOf<Pair<String, Int>>()

	inline fun <reified T> emit(data: T) {
		val eventName = normalizeEventName(T::class.toString())

		emit(eventName, data)
	}

	fun <T> emit(eventName: String, data: T) {

		setTimeout {

			if (!eventsHandlers.containsKey(eventName)) {
				eventsHandlers[eventName] = mutableListOf()
			}

			val eventHandlers = eventsHandlers[eventName]

			Console.debug("local event $LOCAL_EVENT_PREFIX$eventName sent")

			eventHandlers?.forEach {
				it(data)
			}
		}
	}

	inline fun <reified T> on(noinline function: (T) -> Unit): Int {
		val eventName = normalizeEventName(T::class.toString())

		return on(eventName, function)
	}

	fun <T> on(eventName: String, function: (T) -> Unit): Int {
		if (!eventsHandlers.containsKey(eventName)) {
			eventsHandlers[eventName] = mutableListOf()
		}

		val eventHandlers = eventsHandlers[eventName]!!

		Console.info("local event $eventName registered")

		eventHandlers.add(function)

		return registerEvent(eventName, eventHandlers.lastIndex)
	}

	fun unSubscribe(id: Int) {
		val event: Pair<String, Int>? = eventsHandlersRegistry.get(id)

		event?.let {
			eventsHandlers[it.first]?.apply {
				removeAt(it.second)
			}
		}
	}

	private fun registerEvent(eventName: String, index: Int): Int {
		eventsHandlersRegistry.add(eventName to index)
		return eventsHandlersRegistry.lastIndex
	}
}
