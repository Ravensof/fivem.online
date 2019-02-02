package online.fivem.common.entities

import kotlinx.serialization.Serializable

@Serializable
class InternetRadioStation(
	val url: String,
	val name: String? = null,
	val defaultVolume: Double = 1.0
)