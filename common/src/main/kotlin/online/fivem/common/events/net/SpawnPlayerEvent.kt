package online.fivem.common.events.net

import online.fivem.common.entities.CoordinatesX
import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class SpawnPlayerEvent(
	val coordinatesX: CoordinatesX,
	val model: Int? = null
) : Serializable()