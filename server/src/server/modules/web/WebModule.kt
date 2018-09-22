package server.modules.web

import server.modules.AbstractModule
import universal.extensions.onNull

class WebModule : AbstractModule() {
	init {

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