package universal.extensions

fun Float?.orZero(): Float {
	return this ?: 0f
}