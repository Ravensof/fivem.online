package online.fivem.client.common

import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.events.net.AcceptEvent
import online.fivem.common.events.nui.WebStorageEvent
import online.fivem.common.extensions.forEach
import kotlin.random.Random

object WebStorage {

	suspend fun get(key: String): String? {

		val channel = NuiEvent.openSubscription(WebStorageEvent.Response::class)
		val requestId = channel.hashCode()

		NuiEvent.emit(
			WebStorageEvent.Request(
				requestId = requestId,
				key = key
			)
		)

		channel.forEach {
			if (it.responseId != requestId) return@forEach
			channel.cancel()
			return it.data
		}

		return null
	}

	suspend fun set(key: String, value: String) {
		val eventId = Random.nextInt()

		val channel = NuiEvent.openSubscription(AcceptEvent::class)

		NuiEvent.emit(
			WebStorageEvent.Post(
				key = key,
				value = value,
				eventId = eventId
			)
		)

		channel.forEach {
			if (it.eventId == eventId) channel.cancel()
		}
	}
}