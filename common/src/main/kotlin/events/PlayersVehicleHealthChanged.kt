package online.fivem.common.events

class PlayersVehicleHealthChanged(
	val bodyHealth: Int,
	val bodyDiff: Int,

	val engineHealth: Double,
	val engineDiff: Double,

	val petrolTankHealth: Double,
	val petrolTankDiff: Double
)