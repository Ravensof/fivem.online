package online.fivem.common.events.net

import online.fivem.common.entities.CoordinatesX
import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class SpawnVehiclesCommand(
	val vehicles: List<Vehicle>

) : Serializable() {

	@kotlinx.serialization.Serializable
	class Vehicle(
		val id: Int,
		val modelHash: Int,
		val coordinatesX: CoordinatesX
	)
}