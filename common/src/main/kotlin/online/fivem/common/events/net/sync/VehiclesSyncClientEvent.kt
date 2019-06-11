package online.fivem.common.events.net.sync

import online.fivem.common.entities.CoordinatesX
import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class VehiclesSyncClientEvent(
	val vehicles: List<Vehicle>

) : Serializable() {

	@kotlinx.serialization.Serializable
	class Vehicle(
		val networkId: Int,
		val coordinatesX: CoordinatesX

	)
}