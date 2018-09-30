package universal.modules.radio

import universal.events.IEvent

class InternetRadioStation(
		val url: String,
		val defaultVolume: Double = 1.0
) : IEvent()