package online.fivem.common.extensions

import kotlinx.coroutines.channels.ReceiveChannel

suspend fun <T> ReceiveChannel<T>.forEach(function: suspend (T) -> Unit) {
	for (data in this) {
		function(data)
	}
}