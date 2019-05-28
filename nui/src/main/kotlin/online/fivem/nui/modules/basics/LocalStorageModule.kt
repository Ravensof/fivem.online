package online.fivem.nui.modules.basics

import kotlinx.coroutines.launch
import online.fivem.common.events.net.AcceptEvent
import online.fivem.common.events.nui.WebStorageEvent
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import kotlin.browser.localStorage
import kotlin.coroutines.CoroutineContext

class LocalStorageModule(override val coroutineContext: CoroutineContext) : AbstractNuiModule() {

	init {
		ClientEvent.on<WebStorageEvent.Request> {
			ClientEvent.emit(
				WebStorageEvent.Response(
					responseId = it.requestId,
					data = get(it.key)
				)
			)
		}
		ClientEvent.on<WebStorageEvent.Post> { set(it.key, it.value, it.eventId) }
	}

	fun get(key: String): String? {
		return LS.getItem(key)
	}

	fun set(key: String, value: String, eventId: Int) = launch {
		LS.setItem(key, value)

		ClientEvent.emit(AcceptEvent(eventId))
	}

	companion object {
		private val LS = localStorage
	}
}

