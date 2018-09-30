package client.modules.eventGenerator.events.controls

import universal.events.IEvent
import universal.r.Controls

class ControlJustReleasedEvent(
		val control: Controls.Keys
) : IEvent()