package online.fivem.common.events

import online.fivem.common.entities.InternetRadioStation

class InternetRadioChangedEvent(
	val internetRadioStation: InternetRadioStation
)

class InternetRadioStopEvent

class InternetRadioVolumeChangeEvent(
	val volume: Double
)