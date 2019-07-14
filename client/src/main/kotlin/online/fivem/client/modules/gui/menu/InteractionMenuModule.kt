package online.fivem.client.modules.gui.menu

import kotlinx.coroutines.async
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.gui.NavigationControlHelperModule
import online.fivem.common.common.Console

class InteractionMenuModule(
	private val navigationControlHelperModule: NavigationControlHelperModule
) : AbstractClientModule(), NavigationControlHelperModule.Listener {


	override fun onStartAsync() = async {
		navigationControlHelperModule.waitForStart()


	}


	override fun onKeyExit() {
		navigationControlHelperModule.listener = null
		Console.debug("InteractionMenuModule::onKeyExit")
	}

	override fun onFocus() {
		Console.debug("InteractionMenuModule::onFocus")
	}

	override fun onFocusLost() {
		Console.debug("InteractionMenuModule::onFocusLost")
	}


	fun onOpen(): Boolean {
		navigationControlHelperModule.listener = this

		return true
	}


}