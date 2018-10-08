package client.modules.gui

import client.extensions.*
import universal.common.Console
import universal.common.Event
import universal.common.clearInterval
import universal.common.setInterval
import universal.modules.gui.MenuItem
import universal.modules.gui.events.CompoundMenuCloseEvent
import universal.modules.gui.events.CompoundMenuFocusItemEvent
import universal.modules.gui.events.CompoundMenuShowEvent
import universal.r.Controls

class CompoundMenu(val menuItem: MenuItem, val columns: Int = 1) {

	private var onDetach: () -> Unit = {}
	private var selectedItem = -1
	private var countItems = menuItem.countItems()

	fun show(): Boolean {
		selectedItem = -1

		if (active == null) {
			active = this

			Event.emitNui(CompoundMenuShowEvent(menuItem))

			return true
		}

		return false
	}

	fun forceAttach() {
		active?.close()
		active = this
	}

	fun close() {
		onDetach()
		active = null
		Event.emitNui(CompoundMenuCloseEvent())
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
		Console.logWeb("key Cancel pressed")
	}

	private fun onKeyOption() {
		Console.logWeb("key Option pressed")
	}

	private fun onKeyExtraOption() {
		Console.logWeb("key ExtraKey pressed")
	}

	private fun onKeyExit() {
		Console.logWeb("key Exit pressed")
	}

	companion object {

		private const val KEY_INTERVAL_CLICK = 500

		private var keyUp: dynamic = null
		private var keyDown: dynamic = null
		private var keyRight: dynamic = null
		private var keyLeft: dynamic = null

		init {
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_UP) { active?.onKeyUp() }
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_DOWN) { active?.onKeyDown() }
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_LEFT) { active?.onKeyLeft() }
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_RIGHT) { active?.onKeyRight() }

			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_SELECT) { active?.onKeySelect() }//LEFT MOUSE | ENTER
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_CANCEL) { active?.onKeyCancel() }//RIGHT MOUSE| BACKSPACE

			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_OPTION) { active?.onKeyOption() }// DEL
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_EXTRA_OPTION) { active?.onKeyExtraOption() }// MIDDLE MOUSE
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_SCROLL_FORWARD) { active?.onScrollDown() }//MOUSE SCROLL DOWN
			Controls.onKeyShortPressed(Controls.Keys.INPUT_CELLPHONE_SCROLL_BACKWARD) { active?.onScrollUp() }//MOUSE SCROLL UP

			Controls.onKeyJustPressed(Controls.Keys.INPUT_CELLPHONE_UP) { keyUp = setInterval(KEY_INTERVAL_CLICK) { active?.onKeyUp() } }
			Controls.onKeyJustReleased(Controls.Keys.INPUT_CELLPHONE_UP) { clearInterval(keyUp) }

			Controls.onKeyJustPressed(Controls.Keys.INPUT_CELLPHONE_DOWN) { keyDown = setInterval(KEY_INTERVAL_CLICK) { active?.onKeyDown() } }
			Controls.onKeyJustReleased(Controls.Keys.INPUT_CELLPHONE_DOWN) { clearInterval(keyDown) }

			Controls.onKeyJustPressed(Controls.Keys.INPUT_CELLPHONE_LEFT) { keyLeft = setInterval(KEY_INTERVAL_CLICK) { active?.onKeyLeft() } }
			Controls.onKeyJustReleased(Controls.Keys.INPUT_CELLPHONE_LEFT) { clearInterval(keyLeft) }

			Controls.onKeyJustPressed(Controls.Keys.INPUT_CELLPHONE_RIGHT) { keyRight = setInterval(KEY_INTERVAL_CLICK) { active?.onKeyRight() } }
			Controls.onKeyJustReleased(Controls.Keys.INPUT_CELLPHONE_RIGHT) { clearInterval(keyRight) }

			Controls.onKeyLongPressed(Controls.Keys.INPUT_CELLPHONE_CANCEL) { active?.onKeyExit() }
		}

		var active: CompoundMenu? = null

	}
}