package online.fivem.client.modules.gui.menu

import kotlinx.coroutines.CoroutineScope
import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.client.modules.gui.MainControlListener
import online.fivem.client.modules.gui.NavigationControlsHandler
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.gtav.NativeControls
import kotlin.coroutines.CoroutineContext

class PhoneMenu(
	override val coroutineContext: CoroutineContext

) : AbstractModule(),
	CoroutineScope,
	NavigationControlsHandler.Listener {


	private val controlHandlerModule by moduleLoader.delegate<ControlHandlerModule>()
	private var navigatorListener: NavigationControlsHandler? = null

	override fun onInit() {
		moduleLoader.on<MainControlListener>(this) {
			it.onShortPressListener(
				NativeControls.Keys.PHONE,
				::onPhoneOpenButton
			)
		}

		super.onInit()
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