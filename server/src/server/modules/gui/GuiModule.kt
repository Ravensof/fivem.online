package server.modules.gui

import server.extensions.emitNetAll
import server.modules.AbstractModule
import universal.common.Event
import universal.extensions.onNull
import universal.modules.gui.events.ConsoleLogWebEvent
import universal.modules.gui.events.ConsoleWarnWebEvent

class GuiModule : AbstractModule() {
	init {
		Event.on<ConsoleLogWebEvent> { Event.emitNetAll(it) }
		Event.on<ConsoleWarnWebEvent> { Event.emitNetAll(it) }
	}

	companion object {


		private var instance: GuiModule? = null

		fun getInstance(): GuiModule {
			instance.onNull {
				instance = GuiModule()
			}

			return instance!!
		}
	}
}