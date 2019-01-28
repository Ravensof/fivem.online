package online.fivem.nui.modules.clientEventEchanger

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NuiPacket
import online.fivem.common.events.net.ImReadyEvent
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.browser.window
import kotlin.coroutines.CoroutineContext

class ClientEventExchangerModule : AbstractModule(), EventListener, CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	override fun onStart(): Job? {
		window.addEventListener("message", this)

		val channel = Channel<Unit>()

		ClientEvent.on<ImReadyEvent> {
			launch {
				channel.send(Unit)
			}
		}

		launch {
			for (data in ClientEventExchangerModule.channel) {
				jQuery.post("http://${GlobalConfig.MODULE_NAME}/${GlobalConfig.NUI_EVENT_NAME}", data)
			}
		}

		return launch {
			ClientEvent.emit(ImReadyEvent())

			channel.receive()
			channel.close()
		}
	}

	override fun onStop(): Job? {
		window.removeEventListener("message", this)

		return super.onStop()
	}

	override fun handleEvent(event: Event) {
		try {
			val data = event.asDynamic().data as Any
			if (data is String) return

			val packet = Serializer.unpack<NuiPacket>(data)
			ClientEvent.handle(packet.data)
		} catch (exception: Serializer.DeserializationException) {
			Console.warn("wrong packet format")
		}
	}

	companion object {
		val channel = Channel<String>()
	}
}