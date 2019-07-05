package online.fivem.server.common

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.extensions.forEach
import online.fivem.server.entities.Player
import online.fivem.server.entities.PlayerSrc
import online.fivem.server.modules.client_event_exchanger.ClientEvent

abstract class AbstractServerModule : AbstractModule() {

	open fun onSync(player: Player, data: ClientSideSynchronizationEvent): Job? = null

	open fun onSync(exportObject: ServerSideSynchronizationEvent): Job? = null

	protected inline fun <reified T : Any> ClientEvent.on(noinline action: suspend (Player, T) -> Unit): ReceiveChannel<ClientEvent.PlayerParams<T>> {

		val channel = openSubscription(T::class)

		this@AbstractServerModule.launch {
			channel.forEach {
				action(it.player, it.data)
			}
		}

		return channel
	}

	protected inline fun <reified T : Any> ClientEvent.onGuest(noinline action: suspend (PlayerSrc, T) -> Unit): ReceiveChannel<ClientEvent.GuestParams<T>> {

		val channel = openGuestSubscription(T::class)

		this@AbstractServerModule.launch {
			channel.forEach {
				action(it.playerSrc, it.data)
			}
		}

		return channel
	}

}