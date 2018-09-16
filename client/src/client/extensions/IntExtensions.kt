package client.extensions

fun Int?.orZero(): Int {
	return this ?: 0
}