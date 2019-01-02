package online.fivem.common.events

import online.fivem.common.gtav.NativeControls

open class ControlChangedEvent(val control: NativeControls.Keys, val isPressed: Boolean)

class ControlJustPressedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, true)

class ControlJustReleasedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, false)

class ControlLongPressedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, true)

class ControlShortPressedEvent(
	control: NativeControls.Keys
) : ControlChangedEvent(control, true)