package online.fivem.common.events

import online.fivem.common.gtav.NativeControls

class ControlJustReleasedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, false)