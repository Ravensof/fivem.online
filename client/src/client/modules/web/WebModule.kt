package client.modules.web

import MODULE_FOLDER_NAME
import client.extensions.emitNui
import client.modules.AbstractModule
import client.modules.web.events.WebReceiverReady
import universal.common.Event
import universal.extensions.onNull

class WebModule : AbstractModule() {
	init {
		Event.emitNui(WebReceiverReady(MODULE_FOLDER_NAME))

	}

	companion object {


		private var instance: WebModule? = null

		fun getInstance(): WebModule {
			instance.onNull {
				instance = WebModule()
			}

			return instance!!
		}
	}
}