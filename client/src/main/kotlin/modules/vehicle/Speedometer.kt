package online.fivem.client.modules.vehicle

import kotlinx.coroutines.*
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.events.*
import online.fivem.common.extensions.orZero
import kotlin.coroutines.CoroutineContext

class Speedometer : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = Job()

	private var vehicleHasSpeedo = false
	private var updateJob: Job? = null

	override fun init() {
		UEvent.on<PlayerGetInDriversSeatEvent> { onPlayerJoinVehicle() }
		UEvent.on<PlayerLeftVehicleEvent> { onPlayerLeftVehicle() }
	}

	private fun updateJob(): Job = launch {
		val ped = Client.getPlayerPed()
		val vehicle = Client.getVehiclePedIsUsing(ped) ?: return@launch

		var speed: Double

		while (isActive) {
			speed = Client.getVehicleDashboardSpeed(vehicle)

			if (!vehicleHasSpeedo && speed > 0) {
				vehicleHasSpeedo = true
				NuiEvent.emit(SpeedometerEnableEvent())
			}

			if (vehicleHasSpeedo) {
				NuiEvent.emit(
					SpeedometerUpdateEvent(
						currentGear = Client.getVehicleCurrentGear(vehicle),
						currentRpm = Client.getVehicleCurrentRpm(vehicle),
						dashboardSpeed = speed,
						turboPressure = Client.getVehicleTurboPressure(vehicle),
						handbrake = Client.getVehicleHandbrake(vehicle),

						engineTemperature = Client.getVehicleEngineTemperature(vehicle),
						fuelLevel = Client.getVehicleFuelLevel(vehicle),
						oilLevel = Client.getVehicleOilLevel(vehicle)?.toDouble().orZero(),
						petrolTankHealth = Client.getVehiclePetrolTankHealth(vehicle),
						engineRunning = Client.getIsVehicleEngineRunning(vehicle),
						engineOn = Client.isVehicleEngineOn(vehicle),
						engineHealth = Client.getVehicleEngineHealth(vehicle)
					)
				)
			}
			delay(UPDATE_RATE)
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