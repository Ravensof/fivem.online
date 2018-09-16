package shared


fun setInterval(timeout: Int = 1000, handler: () -> Unit): Float {
	return setInterval(handler, timeout)
}

fun setTimeout(timeout: Int = 0, handler: () -> Unit): Float {
	return setTimeout(handler, timeout)
}

fun normalizeEventName(eventClass: String): String {
	return eventClass.replace("[A-z]+ (.*)".toRegex(), "$1").replace(" ", "_")
}


private external fun setInterval(handler: Any, timeout: Int): Float

private external fun setTimeout(handler: Any, timeout: Int): Float

external fun clearInterval(handleId: Float)

external fun clearTimeout(handleId: Float)

external fun encodeURIComponent(str: Any): String