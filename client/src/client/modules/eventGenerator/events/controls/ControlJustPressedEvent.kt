package client.modules.eventGenerator.events.controls

import universal.events.IEvent
import universal.r.Controls

class ControlJustPressedEvent(
		val control: Controls.Keys
) : IEvent()