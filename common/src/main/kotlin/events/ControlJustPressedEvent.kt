package online.fivem.common.events

import online.fivem.common.gtav.NativeControls

class ControlJustPressedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, true)
