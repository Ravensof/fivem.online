package online.fivem.client.modules.vehicle

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.entities.Vehicle
import online.fivem.client.events.PlayerLeftOrJoinVehicleEvent
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.Event
import online.fivem.common.events.nui.SpeedometerDisableEvent
import online.fivem.common.events.nui.SpeedometerEnableEvent
import online.fivem.common.events.nui.SpeedometerUpdateEvent
import online.fivem.common.extensions.repeatJob

class Speedometer : AbstractClientModule() {
	private var vehicleHasSpeedo = false
	private var updateJob: Job? = null

	override fun onInit() {
		Event.apply {
			on<PlayerLeftOrJoinVehicleEvent.Join.Driver> { onPlayerJoinVehicle(it.vehicle) }
			on<PlayerLeftOrJoinVehicleEvent.Left> { onPlayerLeftVehicle() }
		}
	}

	override fun onStop(): Job? {
		updateJob?.cancel()

		return super.onStop()
	}

	private fun onPlayerJoinVehicle(vehicle: Vehicle) {

		var speed: Double

		updateJob = repeatJob(UPDATE_RATE) {
			speed = vehicle.dashboardSpeed

			if (!vehicleHasSpeedo && speed > 0) {
				vehicleHasSpeedo = true
				NuiEvent.emit(SpeedometerEnableEvent())
			}

			if (vehicleHasSpeedo) {
				NuiEvent.emit(
					SpeedometerUpdateEvent(
						currentGear = vehicle.currentGear,
						currentRpm = vehicle.currentRpm,
						dashboardSpeed = speed,
						turboPressure = vehicle.turboPressure,
						handbrake = vehicle.isHandBrake,

						engineTemperature = vehicle.engineTemperature,
						fuelLevel = vehicle.fuelLevel,
						oilLevel = vehicle.oilLevel,
						petrolTankHealth = vehicle.petrolTankHealth,
						isEngineRunning = vehicle.isEngineRunning(),
						engineOn = vehicle.isEngineOn,
						engineHealth = vehicle.engineHealth
					)
				)
			}
		}
	}

	private suspend fun onPlayerLeftVehicle() {
		updateJob?.cancelAndJoin()
		vehicleHasSpeedo = false
		NuiEvent.emit(SpeedometerDisableEvent())
	}

	companion object {
		private const val TARGET_FPS = 5
		private const val UPDATE_RATE = 1_000L / TARGET_FPS
	}
}