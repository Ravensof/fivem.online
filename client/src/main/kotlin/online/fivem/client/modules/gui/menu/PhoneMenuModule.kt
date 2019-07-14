package online.fivem.client.modules.gui.menu

import kotlinx.coroutines.async
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.gui.NavigationControlHelperModule
import online.fivem.common.common.Console

class PhoneMenuModule(
	private val navigationControlHelperModule: NavigationControlHelperModule

) : AbstractClientModule(),
	NavigationControlHelperModule.Listener {


	override fun onStartAsync() = async {
		navigationControlHelperModule.waitForStart()
	}

	override fun onKeyUp() {
		Console.debug("onKeyUp")
	}

	override fun onKeyDown() {
		Console.debug("onKeyDown")
	}

	override fun onKeyLeft() {
		Console.debug("onKeyLeft")
	}

	override fun onKeyRight() {
		Console.debug("onKeyRight")
	}

	override fun onKeySelect() {
		Console.debug("onKeySelect")
	}

	override fun onKeyCancel() {
		Console.debug("onKeyCancel")
	}

	override fun onKeyOption() {
		Console.debug("onKeyOption")
	}

	override fun onKeyExtraOption() {
		Console.debug("onKeyExtraOption")
	}

	override fun onScrollDown() {
		Console.debug("onScrollDown")
	}

	override fun onScrollUp() {
		Console.debug("onScrollUp")
	}

	override fun onKeyExit() {
		navigationControlHelperModule.listener = null
		Console.debug("onKeyExit")
	}

	override fun onFocus() {
		Console.debug("onFocus")
	}

	override fun onFocusLost() {
		Console.debug("onFocusLost")
	}

	fun onOpen(): Boolean {
		navigationControlHelperModule.listener = this

		return true
	}

}