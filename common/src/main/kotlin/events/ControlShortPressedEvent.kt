package online.fivem.common.events

import online.fivem.common.gtav.NativeControls

class ControlShortPressedEvent(
	control: NativeControls.Keys
) : ControlJustReleasedEvent(control)