package web.modules.gui

import js.externals.jquery.jQuery
import universal.common.clearTimeout
import universal.common.escapeHtml
import universal.common.setTimeout
import universal.extensions.onNull
import universal.modules.AbstractModule
import universal.modules.gui.events.ConsoleLogWebEvent
import universal.modules.gui.events.ConsoleWarnWebEvent
import universal.modules.gui.events.GuiHideEvent
import universal.modules.gui.events.GuiShowEvent
import web.common.Event

class GuiModule private constructor() : AbstractModule() {

	val bodyBlock = jQuery("#content")

	private val debugBlock = jQuery("#debug")
	private val warningBlock = jQuery("#warning")
	private var debugBlockTimeoutId: Float? = null
	private var warningBlockTimeoutId: Float? = null

	private val `interface` = Interface(bodyBlock)

	init {
		Event.onNui<GuiShowEvent> { bodyBlock.show() }
		Event.onNui<GuiHideEvent> { bodyBlock.hide() }

		Event.onNui<ConsoleLogWebEvent> { onConsoleLogWeb(it.text) }
		Event.on<ConsoleLogWebEvent> { onConsoleLogWeb(it.text) }

		Event.onNui<ConsoleWarnWebEvent> { onConsoleWarnWeb(it.text) }
		Event.on<ConsoleWarnWebEvent> { onConsoleWarnWeb(it.text) }
	}

	private fun onConsoleLogWeb(text: String) {
		debugBlock.append("${escapeHtml(text)}<br/>")
		debugBlock.show()

		debugBlockTimeoutId?.let {
			clearTimeout(it)
		}
		debugBlockTimeoutId = setTimeout(HIDE_DEBUG_TIMEOUT) {
			debugBlock.hide()
			debugBlock.html("")
			debugBlockTimeoutId = null
		} as Float
	}

	private fun onConsoleWarnWeb(text: String) {
		warningBlock.append("$text<br/>")
		warningBlock.show()

		warningBlockTimeoutId?.let {
			clearTimeout(it)
		}
		warningBlockTimeoutId = setTimeout(HIDE_DEBUG_TIMEOUT) {
			warningBlock.hide()
			warningBlock.html("")
			warningBlockTimeoutId = null
		} as Float
	}

	companion object {

		private const val HIDE_DEBUG_TIMEOUT = 15_000

		private var instance: GuiModule? = null

		fun getInstance(): GuiModule {
			instance.onNull {
				instance = GuiModule()
			}

			return instance!!
		}
	}
}