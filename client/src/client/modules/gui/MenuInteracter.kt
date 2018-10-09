package client.modules.gui

import client.common.ControlListener
import client.common.ControlManager
import client.extensions.emitNui
import client.extensions.orZero
import universal.common.Console
import universal.common.Event
import universal.common.clearInterval
import universal.common.setInterval
import universal.modules.gui.MenuItem
import universal.modules.gui.events.CompoundMenuCloseEvent
import universal.modules.gui.events.CompoundMenuFocusItemEvent
import universal.modules.gui.events.CompoundMenuShowEvent
import universal.r.Controls.Keys.*

class MenuInteracter : ControlListener.ControlFocusListener {

	private val controlListener = ControlListener()
	var interacterListener: InteracterListener? = null

	var menuItem: MenuItem? = null
	var columns: Int = 1

	private val navigationHistory = mutableListOf<MenuItem>()

	private var selectedItem = -1
	private val countItems: Int
		get() {
			return menuItem?.countItems().orZero()
		}

	private var keyUp: dynamic = null
	private var keyDown: dynamic = null
	private var keyRight: dynamic = null
	private var keyLeft: dynamic = null

	init {
		controlListener.focusListener = this

		controlListener.onKeyShortPressed(INPUT_CELLPHONE_UP) { onKeyUp() }
		controlListener.onKeyShortPressed(INPUT_CELLPHONE_DOWN) { onKeyDown() }
		controlListener.onKeyShortPressed(INPUT_CELLPHONE_LEFT) { onKeyLeft() }
		controlListener.onKeyShortPressed(INPUT_CELLPHONE_RIGHT) { onKeyRight() }

		controlListener.onKeyShortPressed(INPUT_CELLPHONE_SELECT) { onKeySelect() }//LEFT MOUSE | ENTER
		controlListener.onKeyShortPressed(INPUT_CELLPHONE_CANCEL) { onKeyCancel() }//RIGHT MOUSE| BACKSPACE

		controlListener.onKeyShortPressed(INPUT_CELLPHONE_OPTION) { onKeyOption() }// DEL
		controlListener.onKeyShortPressed(INPUT_CELLPHONE_EXTRA_OPTION) { onKeyExtraOption() }// MIDDLE MOUSE
		controlListener.onKeyShortPressed(INPUT_CELLPHONE_SCROLL_FORWARD) { onScrollDown() }//MOUSE SCROLL DOWN
		controlListener.onKeyShortPressed(INPUT_CELLPHONE_SCROLL_BACKWARD) { onScrollUp() }//MOUSE SCROLL UP

		controlListener.onKeyJustPressed(INPUT_CELLPHONE_UP) { keyUp = setInterval(KEY_INTERVAL_CLICK) { onKeyUp() } }
		controlListener.onKeyJustReleased(INPUT_CELLPHONE_UP) { clearInterval(keyUp) }

		controlListener.onKeyJustPressed(INPUT_CELLPHONE_DOWN) { keyDown = setInterval(KEY_INTERVAL_CLICK) { onKeyDown() } }
		controlListener.onKeyJustReleased(INPUT_CELLPHONE_DOWN) { clearInterval(keyDown) }

		controlListener.onKeyJustPressed(INPUT_CELLPHONE_LEFT) { keyLeft = setInterval(KEY_INTERVAL_CLICK) { onKeyLeft() } }
		controlListener.onKeyJustReleased(INPUT_CELLPHONE_LEFT) { clearInterval(keyLeft) }

		controlListener.onKeyJustPressed(INPUT_CELLPHONE_RIGHT) { keyRight = setInterval(KEY_INTERVAL_CLICK) { onKeyRight() } }
		controlListener.onKeyJustReleased(INPUT_CELLPHONE_RIGHT) { clearInterval(keyRight) }

		controlListener.onKeyLongPressed(INPUT_CELLPHONE_CANCEL) { onKeyExit() }
	}

	override fun onFocus() {

	}

	override fun onFocusLost() {
		close()
	}

	fun show() {
		controlListener.focusListener = this
		ControlManager.attach(controlListener)

		selectedItem = -1

		menuItem?.let {
			Event.emitNui(CompoundMenuShowEvent(it))
		}
		interacterListener?.onShow()
	}

	fun close() {
		controlListener.focusListener = null
		ControlManager.release(controlListener)

		Event.emitNui(CompoundMenuCloseEvent())
		interacterListener?.onClose()
	}

	private fun focusItem(index: Int) {
		Event.emitNui(CompoundMenuFocusItemEvent(index))
	}

	private fun onKeyUp() {
		if (selectedItem - columns < 0) {
			selectedItem = countItems - 1
		} else {
			selectedItem -= columns
		}
		focusItem(selectedItem)
	}

	private fun onKeyDown() {
		if (selectedItem + columns >= countItems) {
			selectedItem = 0
		} else {
			selectedItem += columns
		}
		focusItem(selectedItem)
	}

	private fun onScrollUp() {
		if (columns == 1) {
			onKeyUp()
		} else {
			onKeyLeft()
		}
	}

	private fun onScrollDown() {
		if (columns == 1) {
			onKeyDown()
		} else {
			onKeyRight()
		}
	}

	private fun onKeyLeft() {
		if (columns == 1) {
			Console.logWeb("Implement onKeyLeft")
		} else {
			if (selectedItem <= 0) {
				selectedItem = countItems - 1
			} else {
				selectedItem--
			}
		}
		focusItem(selectedItem)
	}

	private fun onKeyRight() {
		if (columns == 1) {
			Console.logWeb("Implement onKeyRight")
		} else {
			if (selectedItem + 1 >= countItems) {
				selectedItem = 0
			} else {
				selectedItem++
			}
		}
		focusItem(selectedItem)
	}

	private fun onKeySelect() {
		Console.logWeb("key Select pressed")
	}

	private fun onKeyCancel() {
		if (navigationHistory.isEmpty()) {
			return onKeyExit()
		}
		Console.logWeb("key Cancel pressed")
	}

	private fun onKeyOption() {
		Console.logWeb("key Option pressed")
	}

	private fun onKeyExtraOption() {
		Console.logWeb("key ExtraKey pressed")
	}

	private fun onKeyExit() {
		close()
	}

	interface InteracterListener {
		fun onClose()
		fun onShow()
	}

	companion object {
		private const val KEY_INTERVAL_CLICK = 250
	}
}