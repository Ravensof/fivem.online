package online.fivem.nui.modules.basics.test

import js.externals.jquery.jQuery
import online.fivem.common.events.nui.BlackOutEvent
import online.fivem.common.events.nui.CancelBlackOutEvent
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class BlackScreenModule(override val coroutineContext: CoroutineContext) :
	AbstractNuiModule() {

	override suspend fun onInit() {
		val blackBlock = jQuery(
			"""
				<div style="
					height: 100%;
					width: 100%;
					background-color:black;
					position: absolute;"></div>
			""".trimIndent()
		)
		blackBlock.hide()
		jQuery("body").prepend(blackBlock)

		ClientEvent.apply {
			on<BlackOutEvent> {
				if (it.duration == 0) {
					blackBlock.show()
				} else {
					blackBlock.fadeIn(it.duration)
				}
			}

			on<CancelBlackOutEvent> {
				blackBlock.fadeOut(it.duration)
			}
		}
	}
}