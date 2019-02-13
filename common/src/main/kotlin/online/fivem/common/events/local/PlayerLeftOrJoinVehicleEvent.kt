package online.fivem.common.events.local

import online.fivem.common.common.Entity

sealed class PlayerLeftOrJoinVehicleEvent(
	val vehicle: Entity
) {
	class Changed(
		vehicle: Entity,
		val previousVehicle: Entity
	) : PlayerLeftOrJoinVehicleEvent(vehicle)

	class Join(
		vehicle: Entity
	) : PlayerLeftOrJoinVehicleEvent(vehicle)

	class Left(
		vehicle: Entity
	) : PlayerLeftOrJoinVehicleEvent(vehicle)
}