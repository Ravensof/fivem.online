package online.fivem.client.extensions

import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.Console
import online.fivem.common.events.nui.DebugNUITextEvent

fun Console.debugWeb(string: String) {
	NuiEvent.emit(DebugNUITextEvent(string))
}