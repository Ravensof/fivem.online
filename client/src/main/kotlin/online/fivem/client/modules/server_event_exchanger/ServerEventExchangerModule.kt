package online.fivem.client.modules.server_event_exchanger

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Natives
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.KSerializer
import online.fivem.common.common.Serializer
import online.fivem.common.events.net.EstablishConnectionEvent
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.events.net.StopResourceEvent
import online.fivem.common.extensions.forEach
import online.fivem.common.other.ClientsNetPacket
import online.fivem.common.other.ServersNetPacket

class ServerEventExchangerModule : AbstractClientModule() {

	var key: Double? = null

	override fun onInit() {
		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { rawPacket: Any ->
			try {
				val packet = rawPacket.unsafeCast<ServersNetPacket>()
				val obj = KSerializer.deserialize(packet.hash, packet.serialized)
					?: throw Exception("wrong net packet format")

				launch {
					ServerEvent.handle(obj)
				}
			} catch (e: Throwable) {
				Console.error("${e.message} ${JSON.stringify(rawPacket)}")
			}
		}

		ServerEvent.on<StopResourceEvent> { moduleLoader.stop() }
	}

	@ExperimentalCoroutinesApi
	override fun onStart(): Job? {

		val pauseChannel = Channel<Boolean>()

		launch {
			ServerEvent.openSubscription(EstablishConnectionEvent::class).forEach {
				key = it.key
				ServerEvent.emit(ImReadyEvent())
				pauseChannel.close()
			}
		}

		launch {
			for (data in channel) {
				Natives.emitNet(
					eventName = GlobalConfig.NET_EVENT_NAME,
					data = ClientsNetPacket(
						hash = KSerializer.getSerializerHash(data::class)
							?: throw KSerializer.UnregisteredClassException(data::class),
						serialized = KSerializer.serialize(data),

						playersCount = Client.getNumberOfPlayers(),
						key = key
					)
				)
			}
		}

		launch {
			Console.log("connecting to server..")
			while (!pauseChannel.isClosedForSend) {
				startHandshaking()
				delay(10_000)
			}
		}

		return launch {
			pauseChannel.forEach { }
			Console.log("connected to server")
		}
	}

	private fun startHandshaking() {
		Natives.emitNet(GlobalConfig.NET_EVENT_ESTABLISHING_NAME, Serializer.prepare(ImReadyEvent()))
	}

	companion object {
		val channel = Channel<Any>()
	}
}