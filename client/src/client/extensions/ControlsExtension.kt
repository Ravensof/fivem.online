package client.extensions

import client.modules.eventGenerator.EventGenerator
import client.modules.eventGenerator.events.controls.ControlJustPressedEvent
import client.modules.eventGenerator.events.controls.ControlJustReleasedEvent
import client.modules.eventGenerator.events.controls.ControlLongPressedEvent
import client.modules.eventGenerator.events.controls.ControlShortPressedEvent
import universal.common.Event
import universal.r.Controls

fun Controls.onKeyJustPressed(key: Controls.Keys, function: () -> Unit) {
	EventGenerator.addListenedKey(key)
	Event.on<ControlJustPressedEvent> { function() }
}

fun Controls.onKeyJustReleased(key: Controls.Keys, function: () -> Unit) {
	EventGenerator.addListenedKey(key)
	Event.on<ControlJustReleasedEvent> { function() }
}

fun Controls.onKeyLongPressed(key: Controls.Keys, function: () -> Unit) {
	EventGenerator.addListenedKey(key)
	Event.on<ControlLongPressedEvent> { function() }
}

fun Controls.onKeyShortPressed(key: Controls.Keys, function: () -> Unit) {
	EventGenerator.addListenedKey(key)
	Event.on<ControlShortPressedEvent> { function() }
}

//fun Controls.onKeyHolds(keys: Controls.Keys, function: ()->Unit){
//	Event.on<ControlJustPressedEvent> { function() }
//}