package online.fivem.common.events.net

class SpeedometerUpdateEvent(
	val currentGear: Int,
	val currentRpm: Double,
	val dashboardSpeed: Double,
//		val nextGear: Int,

	val engineTemperature: Int,
	val fuelLevel: Double,
	val handbrake: Boolean,
	val oilLevel: Float,
	val petrolTankHealth: Double,
	val turboPressure: Float?,
	val engineRunning: Boolean,
	val engineOn: Boolean,
	val engineHealth: Double
)