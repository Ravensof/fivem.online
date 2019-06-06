package online.fivem.common.entities

import kotlinx.serialization.Serializable

@Serializable
class Weather(
	val weatherName: String,
	val appearanceTime: Double,
	val temperature: Double
)