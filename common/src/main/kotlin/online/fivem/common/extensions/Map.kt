package online.fivem.common.extensions

inline fun <K, V> Map<K, V>.forEach(action: (K, V) -> Unit) {
	for ((k, v) in this) {
		action(k, v)
	}
}