package online.fivem.client.modules.gui

import kotlinx.coroutines.Job
import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.common.common.AbstractModule
import online.fivem.common.gtav.NativeControls

class MainControlListener : AbstractModule(), ControlHandlerModule.Listener {
	override val registeredKeys = mutableListOf<NativeControls.Keys>()

	private val shortPressHandlers = mutableMapOf<NativeControls.Keys, () -> Boolean>()
	private val longPressHandlers = mutableMapOf<NativeControls.Keys, () -> Boolean>()

	private val controlHandlerModule by moduleLoader.onReady<ControlHandlerModule>()

	override fun start(): Job? {
		controlHandlerModule.addListener(this)

		return super.start()
	}

	override fun stop(): Job? {
		controlHandlerModule.removeListener(this)

		return super.stop()
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