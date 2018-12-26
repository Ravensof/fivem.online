package online.fivem.server.modules.clientEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NetPacket
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.extensions.onNull
import online.fivem.server.gtav.Natives

class ClientEventExchanger : AbstractModule() {
	override fun start(): Job? {
		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { playerSrc: PlayerSrc, netPacket: dynamic ->
			if (netPacket !is NetPacket) {
				throw ServerEventExchangerException("wrong net packet format")
			}

			ClientEvent.handle(
				Packet(playerSrc, netPacket.data)
			)
		}

		GlobalScope.launch {
			for (data in channel) {

				data.playerSrc?.value?.let { playerSrc ->

					Natives.emitNet(
						eventName = GlobalConfig.NET_EVENT_NAME,
						playerSrc = playerSrc,
						data = Serializer.prepare(
							NetPacket(data = data.data)
						)
					)

				}.onNull {
					TODO("сделать рассылку всем игрокам")
				}
			}
		}

		return super.start()
	}

	class ServerEventExchangerException(message: String) : Throwable(message)

	class Packet(
		val playerSrc: PlayerSrc? = null,
		val data: Any
	)

	companion object {
		val channel = Channel<Packet>()
	}
}