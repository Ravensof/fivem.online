package online.fivem.common.extensions

import kotlinx.coroutines.channels.Channel

suspend fun <T> Channel<T>.forEach(function: (T) -> Unit) {
	for (data in this) {
		function(data)
	}
}