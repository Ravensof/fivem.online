package online.fivem.common.events.net

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import online.fivem.common.entities.CoordinatesX

@Serializable
class SynchronizeEvent(
	@ContextualSerialization
	val coordinatesX: CoordinatesX?
)