package online.fivem.common.common

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import online.fivem.common.GlobalConfig

@Deprecated("use UEvent instead")
@Serializable
abstract class Event {

	companion object {
		private const val LOCAL_EVENT_PREFIX = GlobalConfig.LOCAL_EVENT_PREFIX

		private val eventsHandlers = mutableMapOf<String, MutableList<(dynamic) -> Unit>>()
		private val eventsHandlersRegistry = mutableListOf<Pair<String, Int>>()

		inline fun <reified T> emit(data: T): Job {
			val eventName = normalizeEventName(T::class.toString())

			return emit(eventName, data)
		}

		fun <T> emit(eventName: String, data: T): Job {

			if (!eventsHandlers.containsKey(eventName)) {
				eventsHandlers[eventName] = mutableListOf()
			}

			val eventHandlers = eventsHandlers[eventName]

			if (GlobalConfig.DEBUG_LOCAL_EVENTS) {
				Console.debug("local event $LOCAL_EVENT_PREFIX$eventName sent")
			}

			return GlobalScope.launch {
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
			val event: Pair<String, Int>? = eventsHandlersRegistry[id]

			event?.let {
				eventsHandlers[it.first]?.apply {
					removeAt(it.second)
				}
			}
		}

		fun normalizeEventName(eventClass: String): String {
			return eventClass.replace("[A-z]+ (.*)".toRegex(), "$1").replace(" ", "_")
		}

		private fun registerEvent(eventName: String, index: Int): Int {
			eventsHandlersRegistry.add(eventName to index)
			return eventsHandlersRegistry.lastIndex
		}
	}
}
