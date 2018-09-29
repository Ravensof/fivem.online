package web.modules.gui

import js.externals.jquery.jQuery
import universal.common.clearTimeout
import universal.common.setTimeout
import universal.extensions.onNull
import universal.modules.AbstractModule
import universal.modules.gui.events.ConsoleLogWebEvent
import universal.modules.gui.events.GuiHideEvent
import universal.modules.gui.events.GuiShowEvent
import web.common.Event

class GuiModule private constructor() : AbstractModule() {

	private val bodyBlock = jQuery("#content")
	private val debugBlock = jQuery("#debug")
	private var debugBlockTimeoutId: Float? = null

	init {
		Event.onNui<GuiShowEvent> { bodyBlock.show() }
		Event.onNui<GuiHideEvent> { bodyBlock.hide() }
		Event.onNui<ConsoleLogWebEvent> { onConsoleLogWeb(it.text) }
		universal.common.Event.on<ConsoleLogWebEvent> { onConsoleLogWeb(it.text) }
	}

	private fun onConsoleLogWeb(text: String) {
		debugBlock.show()
		debugBlock.html(debugBlock.html() + "<br/>" + text)

		debugBlockTimeoutId?.let {
			clearTimeout(it)
		}
		debugBlockTimeoutId = setTimeout(HIDE_DEBUG_TIMEOUT) {
			debugBlock.hide()
			debugBlock.html("")
			debugBlockTimeoutId = null
		}
	}

	companion object {

		private const val HIDE_DEBUG_TIMEOUT = 30_000

		private var instance: GuiModule? = null

		fun getInstance(): GuiModule {
			instance.onNull {
				instance = GuiModule()
			}

			return instance!!
		}
	}
}