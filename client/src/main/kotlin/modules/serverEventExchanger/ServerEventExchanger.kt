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

class ServerEventExchanger : AbstractModule() {
	override fun start(): Job? {
		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { netPacket: dynamic ->
			if (netPacket !is NetPacket) {
				throw ServerEventExchangerException("wrong net packet format")
			}

			ServerEvent.handle(netPacket.data)
		}

		GlobalScope.launch {
			for (data in channel) {
				Natives.emitNet(
					eventName = GlobalConfig.NET_EVENT_NAME,
					data = Serializer.prepare(
						NetPacket(data = data)
					)
				)
			}
		}

		return super.start()
	}

	class ServerEventExchangerException(message: String) : Throwable(message)

	companion object {
		val channel = Channel<Any>()
	}
}