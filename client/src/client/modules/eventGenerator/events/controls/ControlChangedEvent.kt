package client.modules.eventGenerator.events.controls

import universal.events.IEvent
import universal.r.Controls

open class ControlChangedEvent(val control: Controls.Keys) : IEvent()