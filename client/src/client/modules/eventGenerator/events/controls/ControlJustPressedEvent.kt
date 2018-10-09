package client.modules.eventGenerator.events.controls

import universal.r.Controls

class ControlJustPressedEvent(
		control: Controls.Keys
) : ControlChangedEvent(control)