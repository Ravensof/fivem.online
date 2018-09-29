package web.common

import DEBUG_NUI
import MODULE_FOLDER_NAME
import universal.common.Console
import universal.common.normalizeEventName
import universal.common.setTimeout
import universal.events.IEvent
import universal.modules.gui.events.WebReceiverReady
import web.struct.HttpRequestType
import kotlin.browser.window

object Event {

	private val eventsHandlers = mutableMapOf<String, MutableList<(dynamic) -> Unit>>()
	private val eventsHandlersRegistry = mutableListOf<Pair<String, Int>>()

	init {

		onNui<WebReceiverReady> {
			MODULE_FOLDER_NAME = it.moduleFolderName
		}

//		document.addEventListener("DOMContentLoaded", fun(event: org.w3c.dom.events.Event) {
//			universal.common.Event.emit(DOMContentLoadedEvent())
//		})

		window.addEventListener("message", fun(event: dynamic) {

			val eventName: String = event.data.event
			val data = event.data.data

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


	inline fun <reified T : IEvent> onNui(noinline function: (T) -> Unit): Int {
		val eventName = normalizeEventName(T::class.toString())

		return onNui<T>(eventName) {
			function(IEvent.unserialize(it))
		}
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
//
//	fun emitNui(data: Any): Int {
//		val eventName = normalizeEventName(data::class.toString())
//
//		Console.debug("nui data sent " + eventName + " " + JSON.stringify(data))
//
//		fetch(23)
//
//		fetch(
//				"http://" + MODULE_FOLDER_NAME + "/" + eventName,
//				{"method": "POST", "body": JSON.stringify(data)}
//		)
//				.then(function (response) {
//					return response.text()
//				})
//				.then(function (body) {
//					callback(body)
//				})
//
//		return NativeEvents.Client.sendNuiMessage(object {
//			val event = eventName
//			val data = data
//		})
//	}
}

external val fetch: dynamic

fun performHttpRequest(url: String, type: HttpRequestType = HttpRequestType.GET, data: Map<String, String>? = null, onSuccess: ((String) -> Unit)? = null) {

	val result = if (type != HttpRequestType.POST) {
		fetch(url, object {
			val method = type.name
		})
	} else {
		val postData = data?.map {
			encodeURIComponent(it.key) + "=" + encodeURIComponent(it.value)
		}?.joinToString("&").orEmpty()

//		fetch(url, object {
//			val method = type.name
//			val body = "test=123"//JSON.stringify(data)//postData
//		})

		fetch(url, object {
			val method = "POST"
			val body = "{test=123}"//JSON.stringify(data)//postData
		})
	}

	result.then { response: dynamic ->
		return@then response.text()
	}.then { body: String ->
		onSuccess?.invoke(body)
	}
}

//fun performHttpRequest(url: String, type: HttpRequestType = HttpRequestType.GET, data: Map<String, String>? = null, headers: Any = object {}, callback: (Int, String, dynamic) -> Unit) {
//
//	val postData = data?.map {
//		encodeURIComponent(it.key) + "=" + encodeURIComponent(it.value)
//	}?.joinToString("&").orEmpty()
//
//	Exports.performHttpRequest(url, callback, type.name, postData, headers)
//}

external fun encodeURIComponent(str: String): String