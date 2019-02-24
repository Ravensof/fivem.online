package online.fivem.client.events

import online.fivem.client.entities.Vehicle

sealed class PlayerTryingToGetVehicleEvent(
	val vehicle: Vehicle
) {
	class Start(vehicle: Vehicle) : PlayerTryingToGetVehicleEvent(vehicle)

	class End(vehicle: Vehicle) : PlayerTryingToGetVehicleEvent(vehicle)
}