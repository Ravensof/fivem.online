package online.fivem.common.extensions

inline fun <reified R> R?.onNull(function: () -> Unit) {
	if (this == null) {
		function()
	}
}