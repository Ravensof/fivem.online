package online.fivem.common.events.nui

import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.other.Serializable

sealed class InternetRadioModuleEvent : Serializable() {

	@kotlinx.serialization.Serializable
	class Changed(val internetRadioStation: InternetRadioStation) : InternetRadioModuleEvent()

	@kotlinx.serialization.Serializable
	class Stop : InternetRadioModuleEvent()

	@kotlinx.serialization.Serializable
	class VolumeChanged(val volume: Double) : InternetRadioModuleEvent()
}