package online.fivem.client.modules.nui_event_exchanger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Exports
import online.fivem.common.GlobalConfig
import online.fivem.common.Serializer
import online.fivem.common.common.Console
import online.fivem.common.common.createSupervisorJob
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.extensions.deserialize
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.serializeToPacket
import online.fivem.common.other.NuiPacket
import kotlin.coroutines.CoroutineContext

class NuiEventExchangerModule : AbstractClientModule() {

	override fun onInit() {
		Exports.onNui(GlobalConfig.NUI_EVENT_NAME) { rawPacket ->
			try {

				val packet = rawPacket.unsafeCast<NuiPacket>()
				val data = Serializer.deserialize(packet)

				launch {
					NuiEvent.handle(data)
				}
			} catch (exception: Throwable) {
				Console.error("NuiEventExchangerModule: ${exception.message}")
			}
		}
	}

	override fun onStart(): Job? {

		launch {
			try {
				for (data in channel) {
					Client.sendNuiMessage(
						NuiPacket(Serializer.serializeToPacket(data))
					)
				}
			} catch (exception: Throwable) {
				Console.error("NuiEventExchangerModule: ${exception.message}")
			}
		}

		val channel = Channel<Unit>()

		NuiEvent.on<ImReadyEvent> {
			channel.close()
		}

		return launch {
			channel.forEach {}
			NuiEvent.emit(ImReadyEvent())
		}
	}

	companion object {
		val channel = Channel<Any>()
	}
}