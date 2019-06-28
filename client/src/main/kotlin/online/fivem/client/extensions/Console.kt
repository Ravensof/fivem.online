package online.fivem.client.extensions

import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.Console
import online.fivem.common.events.nui.DebugNUITextEvent

suspend fun Console.debugWeb(string: String, id: Int) = NuiEvent.emit(DebugNUITextEvent(string, id = id))
