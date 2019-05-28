package online.fivem.client.modules.nui_event_exchanger

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Exports
import online.fivem.common.GlobalConfig
import online.fivem.common.Serializer
import online.fivem.common.common.Console
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.events.net.StopResourceEvent
import online.fivem.common.extensions.deserialize
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.serializeToPacket
import online.fivem.common.other.NuiPacket
import online.fivem.common.other.Serializable

class NuiEventExchangerModule : AbstractClientModule() {

	override suspend fun onInit() {
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

	override fun onStart() = launch {

		Console.log("waiting for nui")

		NuiEvent.openSubscription(ImReadyEvent::class).receiveAndCancel()

		emit(ImReadyEvent())

		Console.log("nui connected")

		startEventSender()
	}

	override fun onStop() = launch {
		NuiEvent.emit(StopResourceEvent())
	}

	private fun startEventSender() = launch {
		for (data in channel) {
			emit(data)
		}
	}

	private fun emit(data: Serializable) {
		Client.sendNuiMessage(
			NuiPacket(Serializer.serializeToPacket(data))
		)
	}

	companion object {
		val channel = Channel<Serializable>()
	}
}