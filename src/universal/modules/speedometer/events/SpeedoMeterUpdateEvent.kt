package universal.modules.speedometer.events

import universal.events.IEvent

class SpeedoMeterUpdateEvent(
		val currentGear: Int,
		val currentRpm: Double,
		val dashboardSpeed: Double,
//		val nextGear: Int,

		val engineTemperature: Int,
		val fuelLevel: Double,
		val handbrake: Boolean,
		val oilLevel: Double,
		val petrolTankHealth: Int,
		val turboPressure: Int,
		val engineRunning: Boolean,
		val engineOn: Boolean,
		val engineHealth: Int
) : IEvent()
