package online.fivem.common.entities

import kotlinx.serialization.Serializable
import online.fivem.common.gtav.NativeWeather

@Serializable
class Weather(
	val weather: NativeWeather,
	val appearanceTime: Double,
	val temperature: Double
)