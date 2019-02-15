package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import online.fivem.common.extensions.forEach
import kotlin.reflect.KClass

private typealias EventType = Any

open class SEvent {

	val channels = mutableMapOf<KClass<out EventType>, BroadcastChannel<EventType>>()

	inline fun <reified T : EventType> openSubscription(): ReceiveChannel<T> {
		return getChannel(T::class).openSubscription()
	}

	inline fun <reified T : EventType> CoroutineScope.on(noinline action: (T) -> Unit) {
		launch {
			openSubscription<T>().forEach(action)
		}
	}

	suspend fun emit(data: EventType) {
		channels.filter {
			it.key.isInstance(data)
		}.forEach {
			it.value.send(data)
		}
	}

	fun <T : EventType> getChannel(kClass: KClass<T>): BroadcastChannel<T> {

		channels[kClass]?.let {
			return it.unsafeCast<BroadcastChannel<T>>()
		}

		val channel = BroadcastChannel<EventType>(1)

		channels[kClass] = channel

		return channel.unsafeCast<BroadcastChannel<T>>()
	}

	companion object : SEvent()
}