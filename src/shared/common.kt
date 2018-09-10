package shared


private external fun setInterval(handler: Any, timeout: Int): Float

fun setInterval(timeout: Int, handler: () -> Unit): Float {
	return setInterval(handler, timeout)
}

fun setInterval(handler: () -> Unit) {
	setInterval(handler, 1000)
}

private external fun setTimeout(handler: Any, timeout: Int): Float

fun setTimeout(timeout: Int, handler: () -> Unit): Float {
	return setTimeout(handler, timeout)
}

fun setTimeout(handler: () -> Unit): Float {
	return setTimeout(handler, 0)
}

external fun clearInterval(handleId: Float)

external fun clearTimeout(handleId: Float)

external fun encodeURIComponent(str: Any): String

fun normalizeEventName(className: String): String {
	return className.replace("[A-z]+ (.*)".toRegex(), "$1").replace(" ", "_")
}