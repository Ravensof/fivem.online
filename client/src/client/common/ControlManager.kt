package client.common

import client.modules.eventGenerator.events.controls.*
import universal.common.Event


object ControlManager {

	private val controlListeners: MutableCollection<IControlListener> = arrayListOf()

	init {
		Event.on<ControlJustPressedEvent> { eventHandler(it) }
		Event.on<ControlJustReleasedEvent> { eventHandler(it) }
		Event.on<ControlShortPressedEvent> { eventHandler(it) }
		Event.on<ControlLongPressedEvent> { eventHandler(it) }
	}

	fun attach(controlListener: IControlListener): Int {
		if (controlListeners.contains(controlListener)) {
			release(controlListener)
		}
		controlListeners.add(controlListener)
		controlListener.onFocus()
		return controlListeners.indexOf(controlListener)
	}

	fun release(listenerId: Int) {
		val controlListener = controlListeners.elementAtOrNull(listenerId)
		controlListener?.let {
			release(it)
		}
	}

	fun release(controlListener: IControlListener) {
		if (controlListeners.contains(controlListener)) {
			controlListeners.remove(controlListener)
			controlListener.onFocusLost()
			controlListeners.lastOrNull()?.onFocus()
		}
	}

	private fun eventHandler(event: ControlChangedEvent) {
		controlListeners.lastOrNull()?.eventHandler(event)
	}
}