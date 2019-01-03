package online.fivem.client.modules.serverEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Natives
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NetPacket
import online.fivem.common.events.EstablishConnection
import online.fivem.common.events.ImReady

class ServerEventExchangerModule : AbstractModule() {

	var key: Long? = null

	override fun start(): Job? {
		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { netPacket: dynamic ->
			if (netPacket !is NetPacket) throw ServerEventExchangerException("wrong net packet format")

			ServerEvent.handle(netPacket.data)
		}

		ServerEvent.on<EstablishConnection> { key = it.key }

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

		Natives.emitNet(GlobalConfig.NET_EVENT_ESTABLISHING_NAME, Serializer.prepare(NetPacket(data = ImReady())))

		return super.start()
	}

	class ServerEventExchangerException(message: String) : Throwable(message)

	companion object {
		val channel = Channel<Any>()
	}
}