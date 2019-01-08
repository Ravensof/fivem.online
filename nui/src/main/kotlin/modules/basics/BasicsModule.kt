package online.fivem.nui.modules.basics

import js.externals.jquery.jQuery
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.events.DebugNUITextEvent
import online.fivem.common.events.HideLoadingScreenEvent
import online.fivem.common.events.ShowGuiEvent
import online.fivem.nui.modules.clientEventEchanger.ClientEvent
import kotlin.js.Date

class BasicsModule : AbstractModule() {

	private val loadingScreenBlock = jQuery("#loadingScreen")
	private val contentBlock = jQuery("#content")

	override fun start(): Job? {

		ClientEvent.on<ShowGuiEvent> { showGui(it.show) }
		ClientEvent.on<DebugNUITextEvent> { onConsoleLogWeb(it.text) }
		ClientEvent.on<HideLoadingScreenEvent> { loadingScreenBlock.hide() }

		return super.start()
	}

	private fun showGui(show: Boolean) {
		if (show) {
			contentBlock.show()
		} else {
			contentBlock.hide()
		}
	}

	private val debugBlock by lazy { jQuery("#debug") }
	private val showJob by lazy {
		GlobalScope.launch {
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