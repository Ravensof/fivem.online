package client.extensions

import client.modules.eventGenerator.EventGenerator
import client.modules.eventGenerator.events.controls.ControlJustPressedEvent
import client.modules.eventGenerator.events.controls.ControlJustReleasedEvent
import client.modules.eventGenerator.events.controls.ControlLongPressedEvent
import client.modules.eventGenerator.events.controls.ControlShortPressedEvent
import universal.common.Event
import universal.r.Controls

fun Controls.onKeyJustPressed(key: Controls.Keys, function: () -> Unit) {
	registerRegularKey(key)
	Event.on<ControlJustPressedEvent> {
		if (it.control == key) {
			function()
		}
	}
}

fun Controls.onKeyJustReleased(key: Controls.Keys, function: () -> Unit) {
	registerRegularKey(key)
	Event.on<ControlJustReleasedEvent> {
		if (it.control == key) {
			function()
		}
	}
}

fun Controls.onKeyLongPressed(key: Controls.Keys, function: () -> Unit) {
	registerRegularKey(key)
	Event.on<ControlLongPressedEvent> {
		if (it.control == key) {
			function()
		}
	}
}

fun Controls.onKeyShortPressed(key: Controls.Keys, function: () -> Unit) {
	EventGenerator.addListenedKey(key)
	Event.on<ControlShortPressedEvent> {
		if (it.control == key) {
			function()
		}
	}
}

private fun registerRegularKey(key: Controls.Keys) {
	if (EventGenerator.isFlashKey(key)) {
		throw RuntimeException("you not allowed to use flash keys as regular ($key)")
	}
	EventGenerator.addListenedKey(key)
}

//fun Controls.onKeyHolds(keys: Controls.Keys, function: ()->Unit){
//	Event.on<ControlJustPressedEvent> { function() }
//}