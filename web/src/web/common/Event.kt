package web.common

import DEBUG_NUI
import MODULE_FOLDER_NAME
import js.externals.jquery.jQuery
import universal.common.Console
import universal.common.normalizeEventName
import universal.common.setTimeout
import universal.events.IEvent
import kotlin.browser.window

object Event {

	private val eventsHandlers = mutableMapOf<String, MutableList<(dynamic) -> Unit>>()
	private val eventsHandlersRegistry = mutableListOf<Pair<String, Int>>()

	init {
		window.addEventListener("message", fun(event: dynamic) {

			val eventName: String = event.data.event
			val data = IEvent.unserialize<Any>(event.data.data)

			setTimeout {
				val eventHandlers = eventsHandlers.get(eventName)

				if (eventHandlers != null && DEBUG_NUI) {
					Console.debug("nui event $eventName triggered")
				}

				eventHandlers?.forEach {
					it(data)
				}
			}
		})

//		window.removeEventListener()
	}

	fun init() {

	}

	inline fun <reified T> emit(data: T) {
		universal.common.Event.emit(data)
	}

	inline fun <reified T> on(noinline function: (T) -> Unit): Int {
		return universal.common.Event.on(function)
	}

	fun unSubscribe(id: Int) {
		universal.common.Event.unSubscribe(id)
	}

	inline fun <reified T : IEvent> onNui(noinline function: (T) -> Unit): Int {
		val eventName = normalizeEventName(T::class.toString())

		return onNui(eventName, function)
	}

	fun <T : IEvent> onNui(eventName: String, function: (T) -> Unit): Int {
		if (!eventsHandlers.containsKey(eventName)) {
			eventsHandlers[eventName] = mutableListOf()
		}

		val eventHandlers = eventsHandlers[eventName]!!

		Console.info("nui event $eventName registered")

		eventHandlers.add(function)

		return registerEvent(eventName, eventHandlers.lastIndex)
	}

	fun emitNui(data: IEvent) {
		val eventName = normalizeEventName(data::class.toString())

		jQuery.post("http://$MODULE_FOLDER_NAME/$eventName", JSON.stringify(data.serialize()))

		if (DEBUG_NUI) {
			Console.debug("nui data sent " + eventName + " " + JSON.stringify(data))
		}
	}

//	fun unSubscribe(id: Int) {
//		val event: Pair<String, Int>? = eventsHandlersRegistry.get(id)
//
//		event?.let {
//			eventsHandlers[it.first]?.apply {
//				removeAt(it.second)
//			}
//		}
//	}

	private fun registerEvent(eventName: String, index: Int): Int {
		eventsHandlersRegistry.add(eventName to index)
		return eventsHandlersRegistry.lastIndex
	}
}
