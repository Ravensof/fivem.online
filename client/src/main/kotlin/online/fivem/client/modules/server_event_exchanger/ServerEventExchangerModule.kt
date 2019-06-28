package online.fivem.client.modules.server_event_exchanger

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Natives
import online.fivem.common.GlobalConfig
import online.fivem.common.Serializer
import online.fivem.common.common.Console
import online.fivem.common.events.net.EstablishConnectionEvent
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.extensions.deserialize
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.serializeToPacket
import online.fivem.common.other.ClientsNetPacket
import online.fivem.common.other.Serializable
import online.fivem.common.other.ServersNetPacket

class ServerEventExchangerModule : AbstractClientModule() {

	var key: Double? = null

	init {
		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { rawPacket: Any ->
			try {
				val packet = rawPacket.unsafeCast<ServersNetPacket>()
				val obj = Serializer.deserialize(packet)

				launch {
					ServerEvent.handle(obj)
				}
			} catch (e: Throwable) {
				Console.error("${e.message} ${JSON.stringify(rawPacket)}")
			}
		}
	}

	@ExperimentalCoroutinesApi
	override fun onStartAsync() = async {

		val key = async { ServerEvent.openSubscription(EstablishConnectionEvent::class).receiveAndCancel().key }

		launch {
			Console.log("connecting to server..")

			while (!key.isCompleted) {
				startHandshaking()
				delay(10_000)
			}
		}

		this@ServerEventExchangerModule.key = key.await()
		emit(ImReadyEvent())

		Console.log("connected to server")

		startEventSender()
	}

	private fun startEventSender() = launch {
		for (data in channel) {
			emit(data)
		}
	}

	private fun emit(data: Serializable) {
		Natives.emitNet(
			eventName = GlobalConfig.NET_EVENT_NAME,
			data = ClientsNetPacket(
				Serializer.serializeToPacket(data),
				playersCount = Client.getNumberOfPlayers(),
				key = key
			)
		)
	}

	private fun startHandshaking() {
		Natives.emitNet(GlobalConfig.NET_EVENT_ESTABLISHING_NAME, Serializer.serialize(ImReadyEvent()))
	}

	companion object {
		val channel = Channel<Serializable>()
	}
}