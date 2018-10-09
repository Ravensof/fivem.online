package client.modules.eventGenerator.events.controls

import universal.r.Controls

class ControlLongPressedEvent(
		control: Controls.Keys
) : ControlChangedEvent(control)