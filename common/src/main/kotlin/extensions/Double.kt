package online.fivem.common.extensions

fun Double?.orOne(): Double {
	return this ?: 1.0
}