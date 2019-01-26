package online.fivem.nui.modules.basics.test

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import online.fivem.common.common.AbstractModule
import online.fivem.common.events.net.BlackOutEvent
import online.fivem.common.events.net.CancelBlackOutEvent
import online.fivem.nui.modules.clientEventEchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class BlackScreenModule(override val coroutineContext: CoroutineContext) :
	AbstractModule(), CoroutineScope {

	override fun init() {
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
		jQuery("body").append(blackBlock)

		ClientEvent.on<BlackOutEvent> {
			if (it.duration == 0) {
				blackBlock.show()
			} else {
				blackBlock.fadeIn(it.duration)
			}
		}
		ClientEvent.on<CancelBlackOutEvent> {
			blackBlock.fadeOut(it.duration)
		}
	}
}