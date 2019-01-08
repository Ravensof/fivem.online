package online.fivem.client.modules.nuiEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Exports
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NuiPacket
import online.fivem.common.events.ImReadyEvent

class NuiEventExchangerModule : AbstractModule() {
	override fun start(): Job? {

		Exports.onNui(GlobalConfig.NUI_EVENT_NAME) {
			NuiEvent.handle(Serializer.unpack(it))
		}

		GlobalScope.launch {
			for (data in channel) {
				Client.sendNuiMessage(
					Serializer.prepare(
						NuiPacket(
							data = data
						)
					)
				)
			}
		}

		val channel = Channel<Unit>()

		NuiEvent.on<ImReadyEvent> {
			GlobalScope.launch {
				channel.send(Unit)
			}
		}

		return GlobalScope.launch {
			channel.receive()
			channel.close()
			NuiEvent.emit(ImReadyEvent())
		}
	}

	companion object {
		val channel = Channel<Any>()
	}
}