package online.fivem.common.events

import online.fivem.common.gtav.NativeControls

open class ControlJustReleasedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, false)