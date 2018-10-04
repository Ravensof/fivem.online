package client.modules.gui

import client.common.Client
import client.extensions.onNui
import universal.common.Event
import universal.common.clearInterval
import universal.common.normalizeEventName
import universal.common.setInterval
import universal.events.IEvent
import universal.modules.gui.events.PacketReceived

object NuiDataTransferHelper {

	private const val RETRIEVE_TIME = 50

	init {
		Event.onNui<PacketReceived> { data, callback ->
			clearInterval(object {
				val id = data.id
			})
		}
	}

	fun emitPacket(deliveryCheck: Boolean = true, data: IEvent) {
		val eventName = normalizeEventName(data::class.toString())
		val data = data.serialize()

		var packetId: dynamic = object {
			val id = null
		}

		if (deliveryCheck) {
			packetId = setInterval(RETRIEVE_TIME) {
				Client.sendNuiMessage(object {
					val event = eventName
					val packetId = packetId.id
					val data = data
				})
			}
		}

		Client.sendNuiMessage(object {
			val event = eventName
			val packetId = packetId.id
			val data = data
		})
	}

}