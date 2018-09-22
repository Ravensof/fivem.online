package web.common

import web.struct.HttpRequestType

//object Event {
//
//	fun <T> onNui(eventName: String, function: (T, (String) -> Unit) -> Unit) {
//		Console.info("nui event $eventName registered")
//		Exports.onNui(eventName) { data: T, callback: (String) -> Unit ->
//			Console.debug("nui event $eventName triggered")
//			function(data, callback)
//		}
//	}
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
//}

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