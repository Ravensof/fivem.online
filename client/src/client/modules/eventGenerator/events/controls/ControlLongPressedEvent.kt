package client.modules.eventGenerator.events.controls

import universal.events.IEvent
import universal.r.Controls

class ControlLongPressedEvent(
		val control: Controls.Keys
) : IEvent()