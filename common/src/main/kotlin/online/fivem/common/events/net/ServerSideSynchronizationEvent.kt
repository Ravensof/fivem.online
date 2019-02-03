package online.fivem.common.events.net

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import online.fivem.common.entities.Weather

@Serializable
data class ServerSideSynchronizationEvent(
	var serverTime: Double,

	@ContextualSerialization
	var weather: Weather? = null

)