package online.fivem.nui.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.events.nui.NuiEmulateKeyDownEvent
import online.fivem.common.events.nui.NuiEmulateKeyJustPressedEvent
import online.fivem.common.events.nui.NuiEmulateKeyUpEvent
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window
import kotlin.coroutines.CoroutineContext

class NavigationKeysEmulatorModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {
	override fun onInit() {
		ClientEvent.apply {
			on<NuiEmulateKeyDownEvent> {
				//			val event=jQuery.Event("keydown", it.code)
				//			jQuery().trigger(event)


				emitEvent(KeyEvent("keydown", it.code))
			}
			on<NuiEmulateKeyUpEvent> {
				emitEvent(KeyEvent("keyup", it.code))
			}
			on<NuiEmulateKeyJustPressedEvent> {
				emitEvent(KeyEvent("keydown", it.code))
				delay(it.durability)
				emitEvent(KeyEvent("keyup", it.code))
			}
		}

		window.addEventListener("keydown", { event: Event ->
			Console.debug("event is KeyEvent = ${event is KeyboardEvent}")

			event as KeyboardEvent

			Console.debug(event.code)
		})
	}

	private fun emitEvent(event: Event) {
		window.dispatchEvent(event)
	}

	class KeyEvent(type: String, override val charCode: Int) : KeyboardEvent(type)
}