package online.fivem.common.extensions

fun Throwable.stackTrace(): String {
	return asDynamic().stack.toString()
}