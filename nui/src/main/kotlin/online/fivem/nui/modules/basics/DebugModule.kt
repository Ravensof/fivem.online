package online.fivem.nui.modules.basics

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.events.nui.DebugNUITextEvent
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.common.View
import online.fivem.nui.modules.basics.test.DebugView
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class DebugModule(
	override val coroutineContext: CoroutineContext,
	private val containerView: View
) : AbstractNuiModule() {

	private val debugBlock by lazy { containerView.view.find("#debug") }
	private var timeLeft: Double = 0.0

	private val showJob by lazy {
		launch {
			while (timeLeft > Date.now()) {
				delay(1_000)
			}
			debugBlock.hide()
		}
	}

	override suspend fun onInit() {
		debugBlock.hide()

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

		debugBlock.append(text)

		showJob.start()
	}

	companion object {
		private const val HIDE_DEBUG_TIMEOUT = 15_000
	}
}