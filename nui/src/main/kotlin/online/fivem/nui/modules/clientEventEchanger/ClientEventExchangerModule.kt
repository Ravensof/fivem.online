package online.fivem.nui.modules.clientEventEchanger

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.KSerializer
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NuiPacket
import online.fivem.common.entities.NuiUnsafePacket
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
				jQuery.post(
					"http://${GlobalConfig.MODULE_NAME}/${GlobalConfig.NUI_EVENT_NAME}",

					JSON.stringify(
						NuiPacket(
							hash = KSerializer.getSerializerHash(data::class)
								?: throw KSerializer.UnregisteredClassException(data::class),
							serialized = KSerializer.serialize(data)
						)
					)
				)
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
		val rawPacket = event.asDynamic().data

		if (rawPacket is String) return

		try {

			if (rawPacket.hash == undefined) return handleUnsafePacket(rawPacket)

			val kotlinXSerializationPacket = rawPacket.unsafeCast<NuiPacket>()
			val data = KSerializer.deserialize(kotlinXSerializationPacket.hash, kotlinXSerializationPacket.serialized)
				?: throw Exception("KSerializer.deserialize returns null")

			ClientEvent.handle(data)
		} catch (exception: Throwable) {
			Console.error(
				"ClientEventExchangerModule: ${exception.message}\r\n " +
						"packet: ${JSON.stringify(rawPacket)}"
			)
		}
	}

	private fun handleUnsafePacket(unsafeData: Any) {
		val data = Serializer.unpack<Any>(unsafeData.unsafeCast<NuiUnsafePacket>().data)
		ClientEvent.handle(data)
	}

	companion object {
		val channel = Channel<Any>()
	}
}