package client.modules.session

import client.extensions.onNet
import client.modules.AbstractModule
import client.modules.session.events.SafeEventsReady
import fivem.events.ClientReady
import universal.common.Event
import universal.extensions.onNull

class SessionModule private constructor() : AbstractModule() {

	init {
		Event.onNet<ClientReady> {
			token = it.token
			Event.emit(SafeEventsReady())
		}

	}

	companion object {
		var token: String? = null
			private set(value) {
				field = value
			}

		private var instance: SessionModule? = null

		fun getInstance(): SessionModule {
			instance.onNull {
				instance = SessionModule()
			}

			return instance!!
		}
	}
}