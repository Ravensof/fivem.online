package online.fivem.common.events

import kotlinx.serialization.Serializable

@Serializable
class InternetRadioVolumeChangeEvent(
	val volume: Double
)