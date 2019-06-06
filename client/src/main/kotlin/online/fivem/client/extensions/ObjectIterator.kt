package online.fivem.client.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.common.IObjectIterator

fun <T> IObjectIterator<T>.toChannel(coroutineScope: CoroutineScope = GlobalScope): Channel<T> {
	val channel = Channel<T>()

	coroutineScope.launch {
		forEach {
			channel.send(it)
		}
	}

	channel.invokeOnClose { close() }

	return channel
}