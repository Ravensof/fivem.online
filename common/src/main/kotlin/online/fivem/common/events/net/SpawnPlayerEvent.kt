package online.fivem.common.events.net

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import online.fivem.common.entities.CoordinatesX

@Serializable
class SpawnPlayerEvent(
	@ContextualSerialization val coordinatesX: CoordinatesX,
	val model: Int? = null
)