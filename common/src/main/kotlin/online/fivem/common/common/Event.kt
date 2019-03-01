package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import online.fivem.common.extensions.forEach
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

open class Event : CoroutineScope {
	override val coroutineContext: CoroutineContext = createJob()

	private val channels = mutableMapOf<KClass<out Any>, BroadcastChannel<Any>>()

	fun <T : Any> openSubscription(kClass: KClass<T>): ReceiveChannel<T> {
		return getChannel(kClass).openSubscription()
	}

	inline fun <reified T : Any> on(
		coroutineScope: CoroutineScope = this,
		noinline action: suspend (T) -> Unit
	) {
		coroutineScope.launch {
			openSubscription(T::class).forEach(action)
		}
	}

	open suspend fun emit(data: Any) {
		channels.filter {
			it.key.isInstance(data)
		}.forEach {
			it.value.send(data)
		}
	}

	private fun <T : Any> getChannel(kClass: KClass<T>): BroadcastChannel<T> {

		channels[kClass]?.let {
			return it.unsafeCast<BroadcastChannel<T>>()
		}

		val channel = BroadcastChannel<Any>(1)

		channels[kClass] = channel

		return channel.unsafeCast<BroadcastChannel<T>>()
	}

	companion object : Event()
}