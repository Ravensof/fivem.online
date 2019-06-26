package online.fivem.nui.modules.basics

import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import kotlinx.coroutines.Job
import online.fivem.common.events.nui.DebugNUITextEvent
import online.fivem.common.extensions.repeatJob
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.common.View
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import org.w3c.dom.HTMLElement
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class DebugModule(
	override val coroutineContext: CoroutineContext,
	private val containerView: View
) : AbstractNuiModule() {

	private val debugBlock by lazy { containerView.view.find("#debug") }

	private val blocks = mutableMapOf<Int, Block>()

	override suspend fun onInit() {
		debugBlock.hide()

		ClientEvent.on<DebugNUITextEvent> { onConsoleLogWeb(it.id, it.text) }
	}

	override fun onStart(): Job? {

		repeatJob(1_000) {

			val time = Date.now()

			blocks.forEach {
				if (it.value.timeToRemove >= time) return@repeatJob

				blocks.remove(it.key)
				it.value.element.remove()

				if (blocks.isEmpty()) {
					debugBlock.hide()
				}
			}
		}

		return super.onStart()
	}

	private fun onConsoleLogWeb(id: Int, text: String) {

		blocks.getOrPut(id) {
			Block(
				element = jQuery("<div></div>")
			).also {
				debugBlock.append(it.element)
				debugBlock.show()
			}
		}.apply {
			timeToRemove = Date.now() + HIDE_DEBUG_TIMEOUT
			element.html(text)
		}
	}

	private companion object {
		const val HIDE_DEBUG_TIMEOUT = 15_000
	}

	private class Block(
		val element: JQuery<HTMLElement>,
		var timeToRemove: Double = 0.0
	)
}