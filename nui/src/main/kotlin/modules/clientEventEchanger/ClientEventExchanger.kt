package online.fivem.nui.modules.clientEventEchanger

import js.externals.jquery.jQuery
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NuiPacket
import online.fivem.common.events.ImReady
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.browser.window

class ClientEventExchanger : AbstractModule(), EventListener {

	override fun start(): Job? {
		window.addEventListener("message", this)

		val channel = Channel<Unit>()

		ClientEvent.on<ImReady> {
			GlobalScope.launch {
				channel.send(Unit)
			}
		}

		GlobalScope.launch {
			for (data in ClientEventExchanger.channel) {
				jQuery.post("http://${GlobalConfig.MODULE_NAME}/${GlobalConfig.NUI_EVENT_NAME}", data)
			}
		}

		return GlobalScope.launch {
			ClientEvent.emit(ImReady())

			channel.receive()
			channel.close()
		}
	}

	override fun stop(): Job? {
		window.removeEventListener("message", this)

		return super.stop()
	}

	override fun handleEvent(event: Event) {
		try {
			val packet = Serializer.unpack<NuiPacket>(event.asDynamic().data)
			ClientEvent.handle(packet.data)
		} catch (exception: Serializer.DeserializationException) {
		}
	}

	companion object {
		val channel = Channel<String>()
	}
}