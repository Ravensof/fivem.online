package online.fivem.common.events

import online.fivem.common.gtav.NativeControls

open class ControlChangedEvent(
	val control: NativeControls.Keys,
	val isPressed: Boolean
)