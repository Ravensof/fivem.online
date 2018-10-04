package universal.common


fun setInterval(timeout: Int = 1000, handler: () -> Unit): dynamic {// возвращает object{id=}
	return setInterval(handler, timeout)
}

fun setTimeout(timeout: Int = 0, handler: () -> Unit): dynamic {
	return setTimeout(handler, timeout)
}

fun normalizeEventName(eventClass: String): String {
	return eventClass.replace("[A-z]+ (.*)".toRegex(), "$1").replace(" ", "_")
}


private external fun setInterval(handler: Any, timeout: Int): dynamic

private external fun setTimeout(handler: Any, timeout: Int): dynamic

external fun clearInterval(handleId: Any)

external fun clearTimeout(handleId: Any)

external fun encodeURIComponent(str: Any): String