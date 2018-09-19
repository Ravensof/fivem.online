package client.modules.session

import client.extensions.onNet
import client.modules.AbstractModule
import client.modules.session.extensions.emitSafeNet
import shared.common.Event
import shared.events.ClientReady
import shared.extensions.onNull

class SessionModule private constructor() : AbstractModule() {

	init {
		Event.onNet<ClientReady> {
			token = it.token

			Event.emitSafeNet("test")
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