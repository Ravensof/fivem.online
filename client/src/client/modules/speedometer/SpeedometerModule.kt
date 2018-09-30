package client.modules.speedometer

import client.common.Client
import client.common.Player
import client.extensions.emitNui
import client.extensions.orZero
import client.modules.AbstractModule
import client.modules.eventGenerator.events.vehicle.PlayerLeftVehicleEvent
import client.modules.eventGenerator.events.vehicle.PlayerSeatChangedEvent
import universal.common.Console
import universal.common.Event
import universal.common.clearInterval
import universal.common.setInterval
import universal.extensions.onNull
import universal.modules.speedometer.events.SpeedoMeterDisableEvent
import universal.modules.speedometer.events.SpeedoMeterEnableEvent
import universal.modules.speedometer.events.SpeedoMeterUpdateEvent

class SpeedometerModule private constructor() : AbstractModule() {

	private var intervalId: Float? = null
	private var vehicleHasSpeedo = false

	init {
		Event.on<PlayerSeatChangedEvent> {
			if (it.seatIndex == -1) {
				onPlayerJoinVehicle()
			}
		}
		Event.on<PlayerLeftVehicleEvent> { onPlayerLeftVehicle() }
		Console.log("Client.getPauseMenuState(): " + Client.getPauseMenuState())
	}

	private fun onPlayerJoinVehicle() {
		val vehicle = Client.getVehiclePedIsUsing(Player.getPed().orZero())

		//проверить данные с пассажиром
//		Console.log("getPassengerSeatOfPedInVehicle(): " + Client.getPassengerSeatOfPedInVehicle(vehicle, Player.getPed()!!))//todo проверить пассажиром и водителем

		intervalId = setInterval(UPDATE_RATE) {

			if (!vehicleHasSpeedo && Client.getVehicleDashboardSpeed(vehicle) > 0) {
				vehicleHasSpeedo = true
				Event.emitNui(SpeedoMeterEnableEvent())
			}

			if (vehicleHasSpeedo) {
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

//		Client.setVehicleFuelLevel(vehicle, 65.0)
//		Client.setVehicleOilLevel(vehicle, 0.0)

	}

	private fun onPlayerLeftVehicle() {
		Event.emitNui(SpeedoMeterDisableEvent())
		intervalId?.let {
			clearInterval(it)
		}
		intervalId = null
		vehicleHasSpeedo = false
	}

	companion object {

		private const val TARGET_FPS = 25
		private const val UPDATE_RATE = 1000 / TARGET_FPS

		private var instance: SpeedometerModule? = null

		fun getInstance(): SpeedometerModule {
			instance.onNull {
				instance = SpeedometerModule()
			}

			return instance!!
		}
	}
}