package client.modules.gui

import MODULE_FOLDER_NAME
import client.extensions.emitNui
import client.extensions.onNet
import client.extensions.onNui
import client.modules.AbstractModule
import client.modules.eventGenerator.events.PauseMenuStateChangedEvent
import fivem.Config
import universal.common.Console
import universal.common.Event
import universal.common.clearInterval
import universal.common.setInterval
import universal.events.NuiReadyEvent
import universal.extensions.onNull
import universal.modules.gui.events.ConsoleLogWebEvent
import universal.modules.gui.events.GuiHideEvent
import universal.modules.gui.events.GuiShowEvent
import universal.modules.gui.events.WebReceiverReady

class GuiModule private constructor() : AbstractModule() {

	init {
		Event.on<ConsoleLogWebEvent> { Event.emitNui(it) }

		Event.onNet<ConsoleLogWebEvent> { Event.emitNui(it) }

		val intervalId = setInterval(25) {
			Event.emitNui(WebReceiverReady(
					moduleFolderName = MODULE_FOLDER_NAME,
					resourcesURL = Config.RESOURCES_URL
			))
		}

		Event.onNui<NuiReadyEvent> { _, _ ->
			clearInterval(intervalId)
			Event.emit(NuiReadyEvent())
		}

		Event.on<PauseMenuStateChangedEvent> { onPauseMenuStateChanged(it.pauseMenuState) }

		Console.info("GuiModule loaded")
	}

	private fun onPauseMenuStateChanged(state: Int) {
		if (state == 0) {
			Event.emitNui(GuiShowEvent())
		} else {
			Event.emitNui(GuiHideEvent())
		}
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