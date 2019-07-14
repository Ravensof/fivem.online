package online.fivem.client.modules.gui

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.client.modules.gui.menu.InteractionMenuModule
import online.fivem.client.modules.gui.menu.PhoneMenuModule
import online.fivem.common.common.Console
import online.fivem.common.gtav.NativeControls

class MainControlListenerModule(
	private val controlHandlerModule: ControlHandlerModule
) : AbstractClientModule(), ControlHandlerModule.Listener {

	override val registeredKeys = setOf(
		NativeControls.Keys.INTERACTION_MENU,
		NativeControls.Keys.PHONE
	)

	private val navigationControlHelperModule = NavigationControlHelperModule(
		controlHandlerModule = controlHandlerModule
	)

	private val phoneMenu = PhoneMenuModule(
		navigationControlHelperModule = navigationControlHelperModule
	)

	private val interactionMenu = InteractionMenuModule(
		navigationControlHelperModule = navigationControlHelperModule
	)

	override suspend fun onInit() {
		moduleLoader.add(navigationControlHelperModule)
		moduleLoader.add(phoneMenu)
		moduleLoader.add(interactionMenu)
	}

	override fun onStartAsync() = async {
		controlHandlerModule.waitForStart()

		controlHandlerModule.addListener(this@MainControlListenerModule)

		phoneMenu.waitForStart()
		interactionMenu.waitForStart()
	}

	override fun onStop(): Job? {
		controlHandlerModule.removeListener(this)

		return super.onStop()
	}

	override fun onShortPressed(control: NativeControls.Keys): Boolean = when (control) {

		NativeControls.Keys.INTERACTION_MENU -> {
			Console.debug(control.name)
			interactionMenu.onOpen()
		}

		NativeControls.Keys.PHONE -> {
			Console.debug(control.name)
			phoneMenu.onOpen()
		}

		else -> {
			false
		}
	}
}