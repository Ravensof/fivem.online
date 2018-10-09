package client.common

import client.modules.eventGenerator.events.controls.ControlChangedEvent

interface IControlListener {
	fun eventHandler(event: ControlChangedEvent)
	fun onFocus()
	fun onFocusLost()
}