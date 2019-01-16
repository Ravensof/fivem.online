package online.fivem.client.modules.eventGenerator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.client.extensions.isDisabledControlJustPressed
import online.fivem.client.extensions.isDisabledControlPressed
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.events.ControlJustPressedEvent
import online.fivem.common.events.ControlJustReleasedEvent
import online.fivem.common.events.ControlLongPressedEvent
import online.fivem.common.events.ControlShortPressedEvent
import online.fivem.common.extensions.repeatJob
import online.fivem.common.gtav.NativeControls
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class KeysHandlerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val tickExecutor by moduleLoader.onReady<TickExecutorModule>()
	private var executorId = -1

	override fun start(): Job? {
		executorId = tickExecutor.addTick(::checkPressedFlashKeys)

		repeatJob(KEY_SCAN_TIME) {
			checkPressedKeys()
		}

		return super.start()
	}

	override fun stop(): Job? {
		tickExecutor.removeTick(executorId)

		return super.stop()
	}

	private fun checkPressedKeys() {
		val group = NativeControls.Groups.MOVE

		pressedKeys.forEachIndexed { index, pair ->

			val isControlPressed =
				Client.isControlPressed(group, pair.first) || Client.isDisabledControlPressed(group, pair.first)

			if (isControlPressed) {

				if (pair.second == 0.0) {
					UEvent.emit(ControlJustPressedEvent(pair.first))

					pressedKeys[index] = pair.first to Date.now()
				} else if (pair.second > 0 && Date.now() - pair.second > KEY_HOLD_TIME) {
					UEvent.emit(ControlLongPressedEvent(pair.first))

					pressedKeys[index] = pair.first to -1.0
				}

			} else {

				if (pair.second != 0.0) {
					if (pair.second != -1.0) {
						UEvent.emit(ControlShortPressedEvent(pair.first))
					} else {
						UEvent.emit(ControlJustReleasedEvent(pair.first))
					}

					pressedKeys[index] = pair.first to 0.0
				}
			}
		}
	}

	private fun checkPressedFlashKeys() {
		val group = NativeControls.Groups.MOVE

		pressedFlashKeys.forEachIndexed { index, pair ->
			val isControlJustPressed =
				Client.isControlJustPressed(group, pair.first) || Client.isDisabledControlJustPressed(group, pair.first)

			if (pair.second != 0.0) {
				if (Date.now() - pair.second >= KEY_DEBOUNCE_TIME) {
					pressedFlashKeys[index] = pair.first to 0.0
				} else {
					return@forEachIndexed
				}
			}

			if (isControlJustPressed) {
				pressedFlashKeys[index] = pair.first to Date.now()
				UEvent.emit(ControlShortPressedEvent(pair.first))
			}
		}
	}

	companion object {

		private val flashKeys = arrayListOf(
			NativeControls.Keys.CELLPHONE_SCROLL_BACKWARD,
			NativeControls.Keys.CURSOR_SCROLL_UP,
			NativeControls.Keys.CELLPHONE_SCROLL_FORWARD,
			NativeControls.Keys.CURSOR_SCROLL_DOWN,
			NativeControls.Keys.PREV_WEAPON,
			NativeControls.Keys.NEXT_WEAPON,
			NativeControls.Keys.VEH_SLOWMO_UD,
			NativeControls.Keys.VEH_SLOWMO_UP_ONLY,
			NativeControls.Keys.VEH_SLOWMO_DOWN_ONLY
		)

		private const val KEY_HOLD_TIME = 250
		private const val KEY_SCAN_TIME = 40L
		private const val KEY_DEBOUNCE_TIME = 75

		fun addListenedKey(control: NativeControls.Keys) {

			if (isFlashKey(control)) {
				pressedFlashKeys.forEach {
					if (it.first == control) return
				}

				pressedFlashKeys.add(control to 0.0)

				return
			}

			pressedKeys.forEach {
				if (it.first == control) return
			}

			pressedKeys.add(control to 0.0)
		}

		fun isFlashKey(control: NativeControls.Keys): Boolean {
			return flashKeys.contains(control)
		}

		private val pressedKeys: MutableList<Pair<NativeControls.Keys, Double>> = mutableListOf()
		private val pressedFlashKeys: MutableList<Pair<NativeControls.Keys, Double>> = mutableListOf()
	}
}