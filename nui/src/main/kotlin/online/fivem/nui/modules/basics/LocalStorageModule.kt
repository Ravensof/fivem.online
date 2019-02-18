package online.fivem.nui.modules.basics

import online.fivem.common.common.AbstractModule
import online.fivem.common.events.nui.LocalStorageEvent
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import kotlin.browser.localStorage

class LocalStorageModule : AbstractModule() {

	init {
		ClientEvent.apply {
			on<LocalStorageEvent.Request> {
				ClientEvent.emit(
					LocalStorageEvent.Response(
						responseId = it.requestId,
						data = get(it.key)
					)
				)
			}
			on<LocalStorageEvent.Post> { set(it.key, it.value) }
		}
	}

	fun get(key: String): String? {
		return LS.getItem(key)
	}

	fun set(key: String, value: String) {
		LS.setItem(key, value)
	}

	companion object {
		private val LS = localStorage
	}
}

