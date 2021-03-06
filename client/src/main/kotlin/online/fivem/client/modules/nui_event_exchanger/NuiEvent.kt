package online.fivem.client.modules.nui_event_exchanger

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import online.fivem.common.other.Serializable
import kotlin.reflect.KClass

object NuiEvent {

	private val channels = mutableMapOf<KClass<out Any>, BroadcastChannel<Any>>()

	suspend fun emit(data: Serializable) {
		NuiEventExchangerModule.channel.send(data)
	}

	suspend fun handle(data: Any) {
		channels.filter {
			it.key.isInstance(data)
		}.forEach {
			it.value.send(data)
		}
	}

	fun <T : Any> openSubscription(kClass: KClass<T>): ReceiveChannel<T> {
		return getChannel(kClass).openSubscription()
	}

	private fun <T : Any> getChannel(kClass: KClass<T>): BroadcastChannel<T> {
		return channels
			.getOrPut(kClass) { BroadcastChannel(1) }
			.unsafeCast<BroadcastChannel<T>>()
	}
}