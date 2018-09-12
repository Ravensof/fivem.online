package shared


private external fun setInterval(handler: Any, timeout: Int): Float

fun setInterval(timeout: Int = 1000, handler: () -> Unit): Float {
	return setInterval(handler, timeout)
}

private external fun setTimeout(handler: Any, timeout: Int): Float

fun setTimeout(timeout: Int = 0, handler: () -> Unit): Float {
	return setTimeout(handler, timeout)
}

external fun clearInterval(handleId: Float)

external fun clearTimeout(handleId: Float)

external fun encodeURIComponent(str: Any): String

fun normalizeEventName(eventObject: Any): String {
	return eventObject::class.toString().replace("[A-z]+ (.*)".toRegex(), "$1").replace(" ", "_")
}

external object Base64 {
	fun toBase64(string: String): String
	fun fromBase64(string: String): String
}