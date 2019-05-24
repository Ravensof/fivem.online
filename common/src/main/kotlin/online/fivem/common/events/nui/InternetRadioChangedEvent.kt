package online.fivem.common.events.nui

import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.other.Serializable

class InternetRadioChangedEvent(
	val internetRadioStation: InternetRadioStation
) : Serializable()