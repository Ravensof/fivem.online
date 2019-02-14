package online.fivem.client.events

class PlayersVehicleHealthChangedEvent(
	val bodyHealth: Int,
	val bodyDiff: Int,

	val engineHealth: Double,
	val engineDiff: Double,

	val petrolTankHealth: Double,
	val petrolTankDiff: Double,

	val pedHealth: Int,
	val pedDiff: Int
)