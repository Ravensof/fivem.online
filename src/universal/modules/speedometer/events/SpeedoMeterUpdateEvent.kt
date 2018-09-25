package universal.modules.speedometer.events

class SpeedoMeterUpdateEvent(
		val currentGear: Int,
		val currentRpm: Double,
		val dashboardSpeed: Double,
//		val nextGear: Int,

		val engineTemperature: Int,
		val fuelLevel: Int,
		val handbrake: Boolean,
		val oilLevel: Float,
		val petrolTankHealth: Int,
		val turboPressure: Int,
		val engineRunning: Boolean,
		val engineOn: Boolean,
		val engineHealth: Int
)