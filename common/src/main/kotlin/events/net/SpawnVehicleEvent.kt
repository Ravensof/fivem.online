package online.fivem.common.events.net

import online.fivem.common.entities.CoordinatesX

class SpawnVehicleEvent(
	val vehicleId: Int,
	val vehicleModel: Int,
	val coordinatesX: CoordinatesX
)