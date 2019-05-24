package online.fivem.common.common

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.reflect.KClass

open class Repository<Type : Any> {
	private val channels = mutableMapOf<KClass<Type>, ConflatedBroadcastChannel<Type>>()

	inline fun <reified T : Type> subscribeOn(): ReceiveChannel<T> {
		return subscribeOn(T::class)
	}

	fun <T : Type> subscribeOn(kClass: KClass<T>): ReceiveChannel<T> {
		return getChannel(kClass).openSubscription()
	}

	fun <T : Type> getChannel(kClass: KClass<out T>): ConflatedBroadcastChannel<T> {
		return channels.getOrPut(kClass.unsafeCast<KClass<Type>>()) {
			ConflatedBroadcastChannel()
		}.unsafeCast<ConflatedBroadcastChannel<T>>()
	}

	fun getChannels() = channels.toMap()

}