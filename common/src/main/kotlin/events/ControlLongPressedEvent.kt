package online.fivem.common.events

import online.fivem.common.gtav.NativeControls

class ControlLongPressedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, true)