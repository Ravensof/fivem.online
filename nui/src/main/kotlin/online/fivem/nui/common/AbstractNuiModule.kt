package online.fivem.nui.common

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.extensions.forEach
import online.fivem.nui.modules.client_event_exchanger.ClientEvent

abstract class AbstractNuiModule : AbstractModule() {

	protected inline fun <reified T : Any> ClientEvent.on(noinline action: suspend (T) -> Unit): ReceiveChannel<T> {

		val channel = openSubscription(T::class)

		this@AbstractNuiModule.launch {
			channel.forEach {
				action(it)
			}
		}

		return channel
	}

}