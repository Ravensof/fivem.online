package online.fivem.client.extensions

import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.common.Console
import online.fivem.common.events.net.DebugNUITextEvent

fun Console.debugWeb(string: String) {
	NuiEvent.emit(DebugNUITextEvent(string))
}