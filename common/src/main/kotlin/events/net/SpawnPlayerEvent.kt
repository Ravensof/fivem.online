package online.fivem.common.events.net

import online.fivem.common.entities.CoordinatesX

class SpawnPlayerEvent(
	val coordinatesX: CoordinatesX,
	val model: Int
)