package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.extensions.disableAllControlsAction
import online.fivem.client.extensions.isControlPressed
import online.fivem.client.extensions.isDisabledControlPressed
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeControls
import kotlin.js.Date

class ControlHandlerModule(
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	private val handlers = mutableListOf<Listener>()

	private val pressedKeys = mutableMapOf<NativeControls.Keys, Double>()

	override fun onStartAsync() = async {
		tickExecutorModule.waitForStart()

		NativeControls.Keys.values().forEach { pressedKeys[it] = KEY_NOT_PRESSED }

		tickExecutorModule.add(this@ControlHandlerModule, ::checkPressedKeys)
	}

	override fun onStop(): Job? {
		tickExecutorModule.remove(this)

		return super.onStop()
	}

	fun addListener(listener: Listener) {
		handlers.lastOrNull()?.onFocusLost()
		handlers.add(listener)
	}

	fun removeListener(listener: Listener) {
		handlers.remove(listener)
		handlers.lastOrNull()?.onFocus()
	}

	private fun getPressedControls(): List<Pair<PressType, NativeControls.Keys>> {

		val dateNow = Date.now()

		return getRegisteredKeys().mapNotNull { control ->
			val isControlPressed = control.isControlPressed() || control.isDisabledControlPressed()

			if (isControlPressed) {

				if (pressedKeys[control] == KEY_NOT_PRESSED) {
					pressedKeys[control] = dateNow

					return@mapNotNull PressType.JUST_PRESSED to control
				} else if (pressedKeys[control].orZero() > 0 && dateNow - pressedKeys[control].orZero() > KEY_HOLD_TIME) {
					pressedKeys[control] = KEY_LONG_PRESSED

					return@mapNotNull PressType.LONG_PRESSED to control
				}

			} else {

				if (pressedKeys[control] != KEY_NOT_PRESSED) {

					val previousPressTime = pressedKeys[control]

					pressedKeys[control] = KEY_NOT_PRESSED

					if (previousPressTime == KEY_LONG_PRESSED) {
						return@mapNotNull PressType.JUST_RELEASED to control
					} else {
						return@mapNotNull PressType.JUST_RELEASED_OR_SHORT_PRESSED to control
					}
				}
			}

			return@mapNotNull null
		}
	}

	private fun checkPressedKeys() {

		val pressedControls = getPressedControls()

		handlers.asReversed().forEach { handler ->

			pressedControls.forEach { pressType, control ->
				if (when (pressType) {
						PressType.JUST_PRESSED -> {
							handler.onJustPressed(control).also {
								if (it) {
									markPressedControlsInvisibleForShortPress()
									NativeControls.disableAllControlsAction()
								}
							}
						}

						PressType.JUST_RELEASED_OR_SHORT_PRESSED -> {
							val onShortPressed = handler.onShortPressed(control)
							val onJustReleased = handler.onJustReleased(control)

							onShortPressed || onJustReleased
						}

						PressType.LONG_PRESSED -> {
							handler.onLongPressed(control)
						}

						PressType.JUST_RELEASED -> {
							handler.onJustReleased(control)
						}

					}
				) return
			}
		}
	}

	//todo сделать, чтобы нажатие пойманное в justPressed обработчиком `A`, в justReleased отправлялось ТОЛЬКО в обработчик `A`
	private fun markPressedControlsInvisibleForShortPress() {
		pressedKeys.forEach { control, time ->
			if (time > 0) {
				pressedKeys[control] = KEY_LONG_PRESSED
			}
		}
	}

	private fun getRegisteredKeys(): Set<NativeControls.Keys> {
		val set = mutableSetOf<NativeControls.Keys>()
		handlers.forEach { set.addAll(it.registeredKeys) }
		return set
	}


	private enum class PressType {
		JUST_PRESSED,
		JUST_RELEASED_OR_SHORT_PRESSED,
		LONG_PRESSED,
		JUST_RELEASED
	}


	private companion object {

		const val KEY_LONG_PRESSED = -1.0
		const val KEY_NOT_PRESSED = 0.0
		const val KEY_HOLD_TIME = 250
	}


	interface Listener {
		val registeredKeys: Set<NativeControls.Keys>

		fun onFocus() {}

		fun onFocusLost() {}

		fun onJustPressed(control: NativeControls.Keys): Boolean = false

		fun onShortPressed(control: NativeControls.Keys): Boolean = false

		fun onLongPressed(control: NativeControls.Keys): Boolean = false

		fun onJustReleased(control: NativeControls.Keys): Boolean = false
	}

}