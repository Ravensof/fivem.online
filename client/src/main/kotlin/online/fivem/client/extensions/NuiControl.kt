package online.fivem.client.extensions

import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.NuiControl
import online.fivem.common.events.nui.NuiControlEvent

suspend fun NuiControl.justPress() = NuiEvent.emit(
	NuiControlEvent.JustPress(
		key = name
	)
)