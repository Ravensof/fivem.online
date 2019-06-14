package online.fivem.client.common

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.extensions.forEach

abstract class AbstractClientModule : AbstractModule() {

	open fun onSync(serverData: ServerSideSynchronizationEvent): Job? = null

	open fun onSaveState(container: ClientSideSynchronizationEvent): Job? = null

	protected inline fun <reified T : Any> NuiEvent.on(noinline action: suspend (T) -> Unit): ReceiveChannel<T> {

		val channel = openSubscription(T::class)

		this@AbstractClientModule.launch {
			channel.forEach {
				action(it)
			}
		}

		return channel
	}

	protected inline fun <reified T : Any> ServerEvent.on(noinline action: suspend (T) -> Unit): ReceiveChannel<T> {

		val channel = openSubscription(T::class)

		this@AbstractClientModule.launch {
			channel.forEach {
				action(it)
			}
		}

		return channel
	}

}