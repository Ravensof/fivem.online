package universal.modules.radio.events

import universal.events.IEvent

class RadioChangeVolumeEvent(
		val volume: Double
) : IEvent()