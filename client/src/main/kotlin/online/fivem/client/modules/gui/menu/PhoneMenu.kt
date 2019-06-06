package online.fivem.client.modules.gui.menu

import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.client.modules.gui.MainControlListenerModule
import online.fivem.client.modules.gui.NavigationControlsHandler
import online.fivem.common.common.Console
import online.fivem.common.gtav.NativeControls

class PhoneMenu(
	private val controlHandlerModule: ControlHandlerModule,
	private val mainControlListenerModule: MainControlListenerModule

) : AbstractClientModule(),
	NavigationControlsHandler.Listener {

	private var navigatorListener: NavigationControlsHandler? = null

	override fun onStart() = launch {
		controlHandlerModule.waitForStart()

		mainControlListenerModule.waitForStart()
		mainControlListenerModule.onShortPressListener(
			NativeControls.Keys.PHONE,
			::onPhoneOpenButton
		)
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
		Console.debug("onKeyExit")
	}

	override fun onFocus() {
		Console.debug("onFocus")
	}

	override fun onFocusLost() {
		Console.debug("onFocusLost")
	}

	private fun onPhoneOpenButton(): Boolean {
		navigatorListener = NavigationControlsHandler(coroutineContext, controlHandlerModule, this)

		return true
	}
}