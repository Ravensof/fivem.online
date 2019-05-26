package online.fivem.common.common

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.reflect.KClass

open class Event {

	private val channels = mutableMapOf<KClass<out Any>, BroadcastChannel<Any>>()

	fun <T : Any> openSubscription(kClass: KClass<T>): ReceiveChannel<T> {
		return getChannel(kClass).openSubscription()
	}

	open suspend fun emit(data: Any) {
		channels.filter {
			it.key.isInstance(data)
		}.forEach {
			it.value.send(data)
		}

		if (data is AbstractProcessEvent<*>) {
			data.start()
		}
	}

	private fun <T : Any> getChannel(kClass: KClass<T>): BroadcastChannel<T> {
		return channels
			.getOrPut(kClass) { BroadcastChannel(1) }
			.unsafeCast<BroadcastChannel<T>>()
	}

	companion object : Event()
}