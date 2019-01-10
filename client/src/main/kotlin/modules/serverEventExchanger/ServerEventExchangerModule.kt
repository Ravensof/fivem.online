package online.fivem.client.modules.serverEventExchanger

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.client.gtav.Natives
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NetPacket
import online.fivem.common.events.EstablishConnectionEvent
import online.fivem.common.events.ImReadyEvent

class ServerEventExchangerModule : AbstractModule() {

	var key: Double? = null

	override fun init() {
		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { netPacket: Any ->
			try {
				val packet = Serializer.unpack<NetPacket>(netPacket)

				ServerEvent.handle(packet.data)
			} catch (e: Throwable) {
				Console.error("${e.message} ${JSON.stringify(netPacket)}")
			} catch (e: Serializer.DeserializationException) {
				Console.error("wrong net packet format")
			}
		}
	}

	@ExperimentalCoroutinesApi
	override fun start(): Job? {

		val pauseChannel = Channel<Boolean>()

		ServerEvent.on<EstablishConnectionEvent> {
			key = it.key
			ServerEvent.emit(ImReadyEvent())
			GlobalScope.launch { pauseChannel.send(true) }
		}

		GlobalScope.launch {
			for (data in channel) {
				Natives.emitNet(
					eventName = GlobalConfig.NET_EVENT_NAME,
					data = Serializer.prepare(
						NetPacket(data = data, key = key)
					)
				)
			}
		}

		GlobalScope.launch {
			while (!pauseChannel.isClosedForSend) {
				startHandshaking()
				delay(5_000)
			}
		}

		return GlobalScope.launch {
			Console.log("connecting to server..")
			pauseChannel.receive()
			pauseChannel.close()
			Console.log("connected to server")
		}
	}

	private fun startHandshaking() {
		Natives.emitNet(GlobalConfig.NET_EVENT_ESTABLISHING_NAME, Serializer.prepare(NetPacket(data = ImReadyEvent())))
	}

	companion object {
		val channel = Channel<Any>()
	}
}