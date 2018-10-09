package client.common

import client.modules.eventGenerator.EventGenerator
import client.modules.eventGenerator.events.controls.*
import universal.r.Controls

class ControlListener : IControlListener {

	var focusListener: ControlFocusListener? = null

	override fun onFocus() {
		focusListener?.onFocus()
	}

	override fun onFocusLost() {
		focusListener?.onFocusLost()
	}

	private val controlsHandlers: MutableMap<Controls.Keys, MutableMap<PressType, () -> Unit>> = mutableMapOf()

	fun onKeyJustPressed(control: Controls.Keys, function: () -> Unit) {
		registerRegularKey(control)
		register(control, PressType.PRESS, function)
	}

	fun onKeyJustReleased(control: Controls.Keys, function: () -> Unit) {
		registerRegularKey(control)
		register(control, PressType.RELEASE, function)
	}

	fun onKeyLongPressed(control: Controls.Keys, function: () -> Unit) {
		registerRegularKey(control)
		register(control, PressType.LONG, function)
	}

	fun onKeyShortPressed(control: Controls.Keys, function: () -> Unit) {
		EventGenerator.addListenedKey(control)
		register(control, PressType.SHORT, function)
	}

	private fun register(control: Controls.Keys, pressType: PressType, handler: () -> Unit) {
		if (!controlsHandlers.containsKey(control)) {
			controlsHandlers.put(control, mutableMapOf())
		}

		val handlers = controlsHandlers.get(control)

		if (handlers?.contains(pressType) == false) {
			handlers.put(pressType, handler)
		} else {
			handlers?.set(pressType, handler)
		}
	}

	private fun registerRegularKey(control: Controls.Keys) {
		if (EventGenerator.isFlashKey(control)) {
			throw RuntimeException("you not allowed to use flash keys ($control) as regular")
		}
		EventGenerator.addListenedKey(control)
	}

	private fun keyHandler(control: Controls.Keys, pressType: PressType) {
		controlsHandlers.get(control)?.get(pressType)?.invoke()
	}

	override fun eventHandler(event: ControlChangedEvent) {
		when (event) {
			is ControlJustPressedEvent -> {
				keyHandler(event.control, PressType.PRESS)
			}
			is ControlJustReleasedEvent -> {
				keyHandler(event.control, PressType.RELEASE)
			}
			is ControlLongPressedEvent -> {
				keyHandler(event.control, PressType.LONG)
			}
			is ControlShortPressedEvent -> {
				keyHandler(event.control, PressType.SHORT)
			}
		}
	}

	interface ControlFocusListener {
		fun onFocus()

		fun onFocusLost()
	}

	enum class PressType {
		PRESS,
		RELEASE,
		SHORT,
		LONG
	}
}