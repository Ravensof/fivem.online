package universal.modules.radio.events

import universal.events.IEvent
import universal.modules.radio.InternetRadioStation

class RadioChangeStationEvent(
		val internetRadioStation: InternetRadioStation,
		val volume: Double
) : IEvent()