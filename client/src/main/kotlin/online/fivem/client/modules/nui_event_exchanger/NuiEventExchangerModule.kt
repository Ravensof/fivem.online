package online.fivem.client.modules.nui_event_exchanger

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Exports
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.KSerializer
import online.fivem.common.common.Serializer
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.extensions.forEach
import online.fivem.common.other.NuiPacket
import online.fivem.common.other.NuiUnsafePacket

class NuiEventExchangerModule : AbstractModule() {

	override fun onInit() {
		Exports.onNui(GlobalConfig.NUI_EVENT_NAME) { rawPacket ->
			try {
				val packet = rawPacket.unsafeCast<NuiPacket>()
				val data = KSerializer.deserialize(packet.hash, packet.serialized)
					?: throw Exception("KSerializer.deserialize returns null")

				launch {
					NuiEvent.handle(Serializer.unpack(data))
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
						NuiPacket(
							hash = KSerializer.getSerializerHash(data::class)
								?: throw KSerializer.UnregisteredClassException(data::class),
							serialized = KSerializer.serialize(data)
						)
					)
				}
			} catch (exception: Throwable) {
				Console.error("NuiEventExchangerModule: ${exception.message}")
			}
		}

		launch {
			try {
				for (data in unsafeChannel) {
					Client.sendNuiMessage(
						NuiUnsafePacket(
							data = Serializer.prepare(data)
						)
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
		val unsafeChannel = Channel<Any>()
	}
}