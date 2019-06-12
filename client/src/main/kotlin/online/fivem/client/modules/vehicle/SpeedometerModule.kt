package online.fivem.client.modules.vehicle

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.entities.Vehicle
import online.fivem.client.events.PlayerLeftOrJoinVehicleEvent
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.Event
import online.fivem.common.events.nui.SpeedometerModuleEvent
import online.fivem.common.extensions.repeatJob

class SpeedometerModule : AbstractClientModule() {

	private var updateJob: Job? = null

	override suspend fun onInit() {
		Event.apply {
			on<PlayerLeftOrJoinVehicleEvent.Join.Driver> { startSpeedometer(it.vehicle) }
			on<PlayerLeftOrJoinVehicleEvent.Left> { onPlayerLeftVehicle() }
		}
	}

	override fun onStop(): Job? {
		updateJob?.cancel()

		return super.onStop()
	}

	private fun startSpeedometer(vehicle: Vehicle) = launch {

		var speed: Double
		var vehicleHasSpeedo = false

		repeatJob(UPDATE_RATE) {
			speed = vehicle.dashboardSpeed

			if (!vehicleHasSpeedo && speed > 0) {
				vehicleHasSpeedo = true
				launch { NuiEvent.emit(SpeedometerModuleEvent.Enable()) }
			}

			if (!vehicleHasSpeedo) return@repeatJob

			NuiEvent.emit(
				SpeedometerModuleEvent.Update(
					currentGear = vehicle.currentGear,
					currentRpm = vehicle.currentRpm,
					dashboardSpeed = speed,
					turboPressure = vehicle.getTurboPressureRPMBased(),
					handbrake = vehicle.isHandBrake


				)
			)
		}

//		repeatJob(UPDATE_RATE * 10) {
//			if (!vehicleHasSpeedo) return@repeatJob
//
//			NuiEvent.emit(
//				SpeedometerModuleEvent.SlowUpadate(
//					engineTemperature = vehicle.engineTemperature,
//					fuelLevel = vehicle.fuelLevel,
//					oilLevel = vehicle.oilLevel,
//					petrolTankHealth = vehicle.petrolTankHealth,
//					isEngineRunning = vehicle.isEngineRunning(),
//					engineOn = vehicle.isEngineOn,
//					engineHealth = vehicle.engineHealth
//				)
//			)
//		}

	}.also {
		updateJob?.cancel()
		updateJob = it
	}

	private fun onPlayerLeftVehicle() = launch {
		updateJob?.cancel()
		NuiEvent.emit(SpeedometerModuleEvent.Disable())
	}

	companion object {
		private const val TARGET_FPS = 5
		private const val UPDATE_RATE = 1_000L / TARGET_FPS
	}
}