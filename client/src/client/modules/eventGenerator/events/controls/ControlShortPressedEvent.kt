package client.modules.eventGenerator.events.controls

import universal.r.Controls

class ControlShortPressedEvent(
		control: Controls.Keys
) : ControlChangedEvent(control)