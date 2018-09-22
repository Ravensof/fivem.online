package universal.extensions

fun <T> Any?.onNull(function: () -> T) {
	if (this == null) {
		function()
	}
}