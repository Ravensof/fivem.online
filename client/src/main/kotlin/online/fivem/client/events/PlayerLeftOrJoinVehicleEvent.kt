package online.fivem.client.events

import online.fivem.client.entities.Vehicle

sealed class PlayerLeftOrJoinVehicleEvent(
	val vehicle: Vehicle
) {
	class Changed(
		vehicle: Vehicle,
		val previousVehicle: Vehicle
	) : PlayerLeftOrJoinVehicleEvent(vehicle)

	sealed class Join(
		vehicle: Vehicle
	) : PlayerLeftOrJoinVehicleEvent(vehicle) {
		class Driver(vehicle: Vehicle) : Join(vehicle)
		class Passenger(vehicle: Vehicle) : Join(vehicle)
	}

	class Left(
		vehicle: Vehicle
	) : PlayerLeftOrJoinVehicleEvent(vehicle)
}