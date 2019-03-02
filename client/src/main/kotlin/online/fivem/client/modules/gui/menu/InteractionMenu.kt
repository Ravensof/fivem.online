package online.fivem.client.modules.gui.menu

import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.client.modules.gui.MainControlListener
import online.fivem.common.gtav.NativeControls

class InteractionMenu : AbstractClientModule() {

	private val controlHandlerModule by moduleLoader.delegate<ControlHandlerModule>()

	override fun onInit() {
		moduleLoader.on<MainControlListener> {
			it.onShortPressListener(
				NativeControls.Keys.INTERACTION_MENU,
				::onInteractionMenuButton
			)
		}

		super.onInit()
	}

	private fun onInteractionMenuButton(): Boolean {

		return true
	}
}