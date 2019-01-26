package online.fivem.common.extensions

fun Double?.orZero(): Double {
	return this ?: 0.0
}

fun Double?.orOne(): Double {
	return this ?: 1.0
}