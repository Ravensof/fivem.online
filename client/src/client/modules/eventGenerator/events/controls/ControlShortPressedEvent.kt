package client.modules.eventGenerator.events.controls

import universal.events.IEvent
import universal.r.Controls

class ControlShortPressedEvent(
		val control: Controls.Keys
) : IEvent()