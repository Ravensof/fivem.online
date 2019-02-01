package online.fivem.common.events.net

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import online.fivem.common.entities.CoordinatesX

@Serializable //todo test
class SpawnVehicleEvent(
	val vehicleId: Int,
	val vehicleModel: Int,
	@ContextualSerialization val coordinatesX: CoordinatesX
)