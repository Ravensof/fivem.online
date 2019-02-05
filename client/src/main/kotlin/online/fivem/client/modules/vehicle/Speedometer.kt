package online.fivem.client.modules.vehicle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.events.PlayerGetInDriversSeatEvent
import online.fivem.common.events.PlayerLeftVehicleEvent
import online.fivem.common.events.nui.SpeedometerDisableEvent
import online.fivem.common.events.nui.SpeedometerEnableEvent
import online.fivem.common.events.nui.SpeedometerUpdateEvent
import online.fivem.common.extensions.orZero
import online.fivem.common.extensions.repeatJob
import kotlin.coroutines.CoroutineContext

class Speedometer(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {
	private var vehicleHasSpeedo = false
	private var updateJob: Job? = null

	override fun onInit() {
		UEvent.on<PlayerGetInDriversSeatEvent> { onPlayerJoinVehicle() }
		UEvent.on<PlayerLeftVehicleEvent> { onPlayerLeftVehicle() }
	}

	private fun updateJob(): Job? {
		val ped = Client.getPlayerPed()
		val vehicle = Client.getVehiclePedIsUsing(ped) ?: return null

		var speed: Double

		return repeatJob(UPDATE_RATE) {
			speed = Client.getVehicleDashboardSpeed(vehicle)

			if (!vehicleHasSpeedo && speed > 0) {
				vehicleHasSpeedo = true
				NuiEvent.emit(SpeedometerEnableEvent())
			}

			if (vehicleHasSpeedo) {
				NuiEvent.emitUnsafe(
					SpeedometerUpdateEvent(
						currentGear = Client.getVehicleCurrentGear(vehicle),
						currentRpm = Client.getVehicleCurrentRpm(vehicle),
						dashboardSpeed = speed,
						turboPressure = Client.getVehicleTurboPressure(vehicle),
						handbrake = Client.getVehicleHandbrake(vehicle),

						engineTemperature = Client.getVehicleEngineTemperature(vehicle),
						fuelLevel = Client.getVehicleFuelLevel(vehicle),
						oilLevel = Client.getVehicleOilLevel(vehicle).orZero(),
						petrolTankHealth = Client.getVehiclePetrolTankHealth(vehicle),
						engineRunning = Client.getIsVehicleEngineRunning(vehicle),
						engineOn = Client.isVehicleEngineOn(vehicle),
						engineHealth = Client.getVehicleEngineHealth(vehicle)
					)
				)
			}
		}
	}

	private fun onPlayerJoinVehicle() {
		updateJob = updateJob()
	}

	private fun onPlayerLeftVehicle() {
		updateJob?.cancel()
		vehicleHasSpeedo = false
		NuiEvent.emit(SpeedometerDisableEvent())
	}

	companion object {
		private const val TARGET_FPS = 5
		private const val UPDATE_RATE = 1_000L / TARGET_FPS
	}
}