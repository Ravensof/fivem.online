package online.fivem.client.modules.gui

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.common.gtav.NativeControls

class MainControlListenerModule(
	private val controlHandlerModule: ControlHandlerModule
) : AbstractClientModule(), ControlHandlerModule.Listener {
	override val registeredKeys = mutableListOf<NativeControls.Keys>()

	private val shortPressHandlers = mutableMapOf<NativeControls.Keys, () -> Boolean>()
	private val longPressHandlers = mutableMapOf<NativeControls.Keys, () -> Boolean>()


	override fun onStartAsync() = async {
		controlHandlerModule.waitForStart()

		controlHandlerModule.addListener(this@MainControlListenerModule)
	}

	override fun onStop(): Job? {
		controlHandlerModule.removeListener(this)

		return super.onStop()
	}

	override fun onShortPressed(control: NativeControls.Keys): Boolean {
		return shortPressHandlers[control]?.invoke() ?: false
	}

	override fun onLongPressed(control: NativeControls.Keys): Boolean {
		return longPressHandlers[control]?.invoke() ?: false
	}

	fun onShortPressListener(control: NativeControls.Keys, callback: () -> Boolean) {
		shortPressHandlers[control] = callback
	}

	fun onLongPressListener(control: NativeControls.Keys, callback: () -> Boolean) {
		longPressHandlers[control] = callback
	}
}