package online.fivem.common.extensions

import kotlinx.coroutines.channels.BroadcastChannel

suspend fun <T> BroadcastChannel<T>.receiveAndCancel(): T {
	return openSubscription().receiveAndCancel()
}