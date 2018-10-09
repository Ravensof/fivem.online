package client.modules.eventGenerator.events.controls

import universal.r.Controls

class ControlJustReleasedEvent(
		control: Controls.Keys
) : ControlChangedEvent(control)