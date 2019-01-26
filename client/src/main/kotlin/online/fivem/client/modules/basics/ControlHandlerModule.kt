package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.client.extensions.disableControlAction
import online.fivem.client.extensions.isControlPressed
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Stack
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeControls
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class ControlHandlerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val handlers = mutableListOf<Listener>()

	private val tickExecutor by moduleLoader.onReady<TickExecutorModule>()
	private var executorId = Stack.UNDEFINED_INDEX

	private val pressedKeys = mutableMapOf<NativeControls.Keys, Double>()

	override fun start(): Job? {
		executorId = tickExecutor.add(::checkPressedKeys)

		return super.start()
	}

	override fun stop(): Job? {
		tickExecutor.remove(executorId)

		return super.stop()
	}

	fun addListener(listener: Listener) {
		handlers.lastOrNull()?.onFocusLost()
		handlers.add(listener)
	}

	fun removeListener(listener: Listener) {
		handlers.remove(listener)
		handlers.lastOrNull()?.onFocus()
	}

	private fun checkPressedKeys() {
		val group = NativeControls.Groups.MOVE

		val activeHandler = handlers.lastOrNull() ?: return

		activeHandler.registeredKeys.forEach {
			val isControlPressed = Client.isControlPressed(group, it)

			if (isControlPressed) {

				if (pressedKeys[it] == 0.0) {
					pressedKeys[it] = Date.now()

					if (activeHandler.onJustPressed(it)) return disableAllKeys()
				} else if (pressedKeys[it].orZero() > 0 && Date.now() - pressedKeys[it].orZero() > KEY_HOLD_TIME) {
					pressedKeys[it] = -1.0

					if (activeHandler.onLongPressed(it)) return disableAllKeys()
				}

			} else {

				if (pressedKeys[it] != 0.0) {
					pressedKeys[it] = 0.0

					if (pressedKeys[it] != -1.0) {
						if (activeHandler.onShortPressed(it)) return disableAllKeys()
					} else {
						if (activeHandler.onJustReleased(it)) return disableAllKeys()
					}
				}
			}
		}
	}

	private fun disableAllKeys() {
		NativeControls.Keys.values().forEach {
			Client.disableControlAction(control = it)
		}
	}

	companion object {

//		private val flashKeys = arrayListOf(
//			NativeControls.Keys.CELLPHONE_SCROLL_BACKWARD,
//			NativeControls.Keys.CURSOR_SCROLL_UP,
//			NativeControls.Keys.CELLPHONE_SCROLL_FORWARD,
//			NativeControls.Keys.CURSOR_SCROLL_DOWN,
//			NativeControls.Keys.PREV_WEAPON,
//			NativeControls.Keys.NEXT_WEAPON,
//			NativeControls.Keys.VEH_SLOWMO_UD,
//			NativeControls.Keys.VEH_SLOWMO_UP_ONLY,
//			NativeControls.Keys.VEH_SLOWMO_DOWN_ONLY
//		)

		private const val KEY_HOLD_TIME = 250
	}

	interface Listener {
		val registeredKeys: List<NativeControls.Keys>

		fun onFocus() {}

		fun onFocusLost() {}

		fun onJustPressed(control: NativeControls.Keys): Boolean = false

		fun onShortPressed(control: NativeControls.Keys): Boolean = false

		fun onLongPressed(control: NativeControls.Keys): Boolean = false

		fun onJustReleased(control: NativeControls.Keys): Boolean = false
	}

//	interface ExtraListener : Listener {
//
//		override val registeredKeys: MutableList<NativeControls.Keys>
//
//		val justHandlers: MutableMap<NativeControls.Keys, () -> Boolean>
//		val shortHandlers: MutableMap<NativeControls.Keys, () -> Boolean>
//		val longHandlers: MutableMap<NativeControls.Keys, () -> Boolean>
//		val releaseHandlers: MutableMap<NativeControls.Keys, () -> Boolean>
//
//		override fun onShortPressed(control: NativeControls.Keys): Boolean = shortHandlers[control]?.invoke() ?: false
//
//		override fun onJustPressed(control: NativeControls.Keys): Boolean = justHandlers[control]?.invoke() ?: false
//
//		override fun onLongPressed(control: NativeControls.Keys): Boolean = longHandlers[control]?.invoke() ?: false
//
//		override fun onJustReleased(control: NativeControls.Keys): Boolean = releaseHandlers[control]?.invoke() ?: false
//
//		fun onShortPressed(control: NativeControls.Keys, callback: () -> Boolean) {
//			if (!registeredKeys.contains(control)) {
//				registeredKeys.add(control)
//			}
//			shortHandlers[control] = callback
//		}
//
//		fun onJustPressed(control: NativeControls.Keys, callback: () -> Boolean) {
//			if (!registeredKeys.contains(control)) {
//				registeredKeys.add(control)
//			}
//			justHandlers[control] = callback
//		}
//
//		fun onLongPressed(control: NativeControls.Keys, callback: () -> Boolean) {
//			if (!registeredKeys.contains(control)) {
//				registeredKeys.add(control)
//			}
//			longHandlers[control] = callback
//		}
//
//		fun onJustReleased(control: NativeControls.Keys, callback: () -> Boolean) {
//			if (!registeredKeys.contains(control)) {
//				registeredKeys.add(control)
//			}
//			releaseHandlers[control] = callback
//		}
//	}
}