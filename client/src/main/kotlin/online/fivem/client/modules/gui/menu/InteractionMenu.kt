package online.fivem.client.modules.gui.menu

import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.client.modules.gui.MainControlListener
import online.fivem.common.common.AbstractModule
import online.fivem.common.gtav.NativeControls

class InteractionMenu : AbstractModule() {

	private val controlHandlerModule by moduleLoader.onReady<ControlHandlerModule>()

	override fun init() {
		moduleLoader.on<MainControlListener> {
			it.onShortPressListener(
				NativeControls.Keys.INTERACTION_MENU,
				::onInteractionMenuButton
			)
		}

		super.init()
	}

	private fun onInteractionMenuButton(): Boolean {

		return true
	}
}