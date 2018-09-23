package universal.extensions

fun Double?.orOne(): Double {

	if (this == null) {
		return 1.0
	}
	return this
}