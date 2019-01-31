package online.fivem.client.modules.serverEventExchanger

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Natives
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Serializer
import online.fivem.common.entities.ClientsNetPacket
import online.fivem.common.entities.ServersNetPacket
import online.fivem.common.events.EstablishConnectionEvent
import online.fivem.common.events.net.ImReadyEvent
import kotlin.coroutines.CoroutineContext

class ServerEventExchangerModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = SupervisorJob()

	var key: Double? = null

	override fun onInit() {
		moduleLoader.add(KotlinSerializationTest())

		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { netPacket: String ->
			try {
				val packet = Serializer.unserialize<ServersNetPacket>(netPacket)

				ServerEvent.handle(packet.data)
			} catch (e: Throwable) {
				Console.error("${e.message} $netPacket")
			} catch (e: Serializer.DeserializationException) {
				Console.error("wrong net packet format")
			}
		}
	}

	@ExperimentalCoroutinesApi
	override fun onStart(): Job? {

		val pauseChannel = Channel<Boolean>()

		ServerEvent.on<EstablishConnectionEvent> {
			key = it.key
			ServerEvent.emit(ImReadyEvent())
			launch { pauseChannel.send(true) }
		}

		launch {
			for (data in channel) {
				Natives.emitNet(
					eventName = GlobalConfig.NET_EVENT_NAME,
					data = Serializer.prepare(
						ClientsNetPacket(
							data = data,

							playersCount = Client.getNumberOfPlayers(),
							key = key
						)
					)
				)
			}
		}

		launch {
			while (!pauseChannel.isClosedForSend) {
				startHandshaking()
				delay(5_000)
			}
		}

		return launch {
			Console.log("connecting to server..")
			pauseChannel.receive()
			pauseChannel.close()
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