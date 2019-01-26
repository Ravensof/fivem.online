package online.fivem.common.extensions

fun Int?.orZero(): Int {
	return this ?: 0
}