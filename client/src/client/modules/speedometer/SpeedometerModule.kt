package client.modules.speedometer

import client.common.Client
import client.common.Player
import client.extensions.emitNui
import client.extensions.orZero
import client.modules.AbstractModule
import client.modules.eventGenerator.events.vehicle.PlayerJoinVehicleEvent
import client.modules.eventGenerator.events.vehicle.PlayerLeftVehicleEvent
import universal.common.Event
import universal.common.clearInterval
import universal.common.setInterval
import universal.extensions.onNull
import universal.modules.speedometer.events.SpeedoMeterDisableEvent
import universal.modules.speedometer.events.SpeedoMeterEnableEvent
import universal.modules.speedometer.events.SpeedoMeterUpdateEvent

class SpeedometerModule private constructor() : AbstractModule() {

	private var intervalId: Float? = null

	init {
		Event.on<PlayerJoinVehicleEvent> { onPlayerJoinVehicle() }
		Event.on<PlayerLeftVehicleEvent> { onPlayerLeftVehicle() }
	}

	private fun onPlayerJoinVehicle() {
		val vehicle = Client.getVehiclePedIsUsing(Player.getPed().orZero())

		Event.emitNui(SpeedoMeterEnableEvent())

		intervalId = setInterval(UPDATE_RATE) {

			Event.emitNui(SpeedoMeterUpdateEvent(
					currentGear = Client.getVehicleCurrentGear(vehicle),
					currentRpm = Client.getVehicleCurrentRpm(vehicle),
					dashboardSpeed = Client.getVehicleDashboardSpeed(vehicle),
					turboPressure = Client.getVehicleTurboPressure(vehicle),
					handbrake = Client.getVehicleHandbrake(vehicle),

					engineTemperature = Client.getVehicleEngineTemperature(vehicle),
					fuelLevel = Client.getVehicleFuelLevel(vehicle),
					oilLevel = Client.getVehicleOilLevel(vehicle),
					petrolTankHealth = Client.getVehiclePetrolTankHealth(vehicle),
					engineRunning = Client.getIsVehicleEngineRunning(vehicle),
					engineOn = Client.isVehicleEngineOn(vehicle),
					engineHealth = Client.getVehicleEngineHealth(vehicle)
			))
		}
	}

	private fun onPlayerLeftVehicle() {
		Event.emitNui(SpeedoMeterDisableEvent())
		intervalId?.let {
			clearInterval(it)
		}
		intervalId = null
	}

	companion object {

		private const val UPDATE_RATE = 25

		private var instance: SpeedometerModule? = null

		fun getInstance(): SpeedometerModule {
			instance.onNull {
				instance = SpeedometerModule()
			}

			return instance!!
		}
	}
}