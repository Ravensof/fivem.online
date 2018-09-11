package shared.extensions

fun <T> List<T>?.nullOnEmpty(): List<T>? {

	return if (this?.isNotEmpty() == true) {
		this
	} else {
		null
	}
}