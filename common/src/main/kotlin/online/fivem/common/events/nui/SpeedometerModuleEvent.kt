package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

sealed class SpeedometerModuleEvent : Serializable() {

	@kotlinx.serialization.Serializable
	class Enable : SpeedometerModuleEvent()

	@kotlinx.serialization.Serializable
	class Disable : SpeedometerModuleEvent()

	@kotlinx.serialization.Serializable
	class Update(
		val currentGear: Int,
		val currentRpm: Double,
		val dashboardSpeed: Double,
		val handbrake: Boolean,
		val turboPressure: Double?

	) : SpeedometerModuleEvent()

	@kotlinx.serialization.Serializable
	class SlowUpadate(
		val engineTemperature: Float,
		val fuelLevel: Double,
		val oilLevel: Float,
		val petrolTankHealth: Double,
		val isEngineRunning: Boolean,
		val engineOn: Boolean,
		val engineHealth: Double
	) : SpeedometerModuleEvent()
}