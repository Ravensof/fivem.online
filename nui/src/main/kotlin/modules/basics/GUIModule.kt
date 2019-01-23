package online.fivem.nui.modules.basics

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.events.DebugNUITextEvent
import online.fivem.common.events.ShowGuiEvent
import online.fivem.nui.modules.clientEventEchanger.ClientEvent
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class GUIModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val container = jQuery("#content")

	override fun init() {
		ClientEvent.on<ShowGuiEvent> { onShowGui(it.show) }
		ClientEvent.on<DebugNUITextEvent> { onConsoleLogWeb(it.text) }
	}

	private fun onShowGui(show: Boolean) {
		if (show) {
			container.show()
		} else {
			container.hide()
		}
	}

	private val debugBlock by lazy { container.find("#debug") }

	private val showJob by lazy {
		launch {
			while (timeLeft > Date.now()) {
				delay(1_000)
			}
			debugBlock.hide()
		}
	}

	private var timeLeft: Double = 0.0

	private fun onConsoleLogWeb(text: String) {
		timeLeft = Date.now() + HIDE_DEBUG_TIMEOUT

		debugBlock.html("")
		debugBlock.show()

		debugBlock.append("${Html.escape(text)}<br/>")

		showJob.start()
	}

	companion object {
		private const val HIDE_DEBUG_TIMEOUT = 15_000
	}
}