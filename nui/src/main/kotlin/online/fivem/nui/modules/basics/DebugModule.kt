package online.fivem.nui.modules.basics

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.events.nui.DebugNUITextEvent
import online.fivem.nui.common.View
import online.fivem.nui.modules.basics.test.DebugView
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class DebugModule(
	override val coroutineContext: CoroutineContext,
	private val containerView: View
) : AbstractModule(), CoroutineScope {

	private val container = jQuery("#content")
	private val debugBlock by lazy { container.find("#debug") }
	private var timeLeft: Double = 0.0

	private val showJob by lazy {
		launch {
			while (timeLeft > Date.now()) {
				delay(1_000)
			}
			debugBlock.hide()
		}
	}

	override fun onInit() {
		ClientEvent.on<DebugNUITextEvent> { onConsoleLogWeb(it.id, it.text) }
	}

	private fun getDebugView(id: Int): DebugView {
		containerView.children.findLast { it is DebugView && it.id == id }?.let {
			return it as DebugView
		}

		val debugView = DebugView(id)
		containerView.add(debugView)

		return debugView
	}

	private fun onConsoleLogWeb(id: Int, text: String) {

//		val debugView = getDebugView(id)

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