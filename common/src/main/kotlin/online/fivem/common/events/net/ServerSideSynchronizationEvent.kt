package online.fivem.common.events.net

import online.fivem.common.entities.Weather
import online.fivem.common.other.Serializable

data class ServerSideSynchronizationEvent(
	var serverTime: Double,

	var weather: Weather? = null
) : Serializable()