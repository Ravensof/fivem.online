package online.fivem.common.events.nui

import kotlinx.serialization.Serializable

@Serializable
class SpeedometerUpdateEvent(
	val currentGear: Int,
	val currentRpm: Double,
	val dashboardSpeed: Double,
//		val nextGear: Int,

	val engineTemperature: Float,
	val fuelLevel: Double,
	val handbrake: Boolean,
	val oilLevel: Float,
	val petrolTankHealth: Double,
	val turboPressure: Int?,
	val isEngineRunning: Boolean,
	val engineOn: Boolean,
	val engineHealth: Double
)