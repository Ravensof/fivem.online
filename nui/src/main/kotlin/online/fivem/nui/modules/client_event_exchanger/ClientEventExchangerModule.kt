package online.fivem.nui.modules.client_event_exchanger

import js.externals.jquery.jQuery
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.Serializer
import online.fivem.common.common.Console
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.extensions.deserialize
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.serializeToPacket
import online.fivem.common.other.NuiPacket
import online.fivem.common.other.Serializable
import online.fivem.nui.common.AbstractNuiModule
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.browser.window

class ClientEventExchangerModule : AbstractNuiModule(), EventListener {

	override fun onStartAsync() = async {
		window.addEventListener("message", this@ClientEventExchangerModule)

		Console.log("connecting to client")

		val waitForClient = launch { ClientEvent.openSubscription(ImReadyEvent::class).receiveAndCancel() }

		while (!waitForClient.isCompleted) {
			emit(ImReadyEvent())
			delay(1_000)
		}

		Console.log("connected to client")

		startEventSender()
	}

	private fun startEventSender() = launch {
		for (data in channel) {
			emit(data)
		}
	}

	private fun emit(data: Serializable) {
		jQuery.post(
			"http://${GlobalConfig.MODULE_NAME}/${GlobalConfig.NUI_EVENT_NAME}",

			JSON.stringify(
				NuiPacket(Serializer.serializeToPacket(data))
			)
		)
	}

	override fun onStop(): Job? {
		window.removeEventListener("message", this)

		return super.onStop()
	}

	override fun handleEvent(event: Event) {
		val rawPacket = event.asDynamic().data

		if (rawPacket is String) return

		try {

			val kotlinXSerializationPacket = rawPacket.unsafeCast<NuiPacket>()
			val data = Serializer.deserialize(kotlinXSerializationPacket)

			launch {
				ClientEvent.handle(data)
			}
		} catch (exception: Throwable) {
			Console.error(
				"ClientEventExchangerModule: ${exception.message}\r\n " +
						"packet: ${JSON.stringify(rawPacket)}"
			)
		}
	}

	companion object {
		val channel = Channel<Serializable>()
	}
}