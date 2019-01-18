package online.fivem.common.extensions

fun Float?.orZero(): Float {
	return this ?: 0f
}