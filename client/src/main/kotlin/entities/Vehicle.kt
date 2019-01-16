package online.fivem.client.entities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import online.fivem.client.extensions.createVehicle
import online.fivem.client.gtav.Client
import online.fivem.client.modules.eventGenerator.TickExecutorModule
import online.fivem.common.common.Entity
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeVehicles

class Vehicle(
	val vehicle: Entity,

	tickExecutor: TickExecutorModule? = null
) {
	val id: Int = Client.getVehicleOilLevel(vehicle)?.toInt().orZero()

	val wheels: List<Wheel>
	val doors: List<Door>

	var clutch: Number
		get() {
			return Client.getVehicleClutch(vehicle)
		}
		set(value) {
			Client.setVehicleClutch(vehicle, value)
		}

	var currentRpm: Double
		set(value) {
			Client.setVehicleCurrentRpm(vehicle, value)
		}
		get() {
			return Client.getVehicleCurrentRpm(vehicle)
		}

	val currentGear: Int
		get() {
			return Client.getVehicleCurrentGear(vehicle)
		}

	var highGear: Int
		get() {
			return Client.getVehicleHighGear(vehicle)
		}
		set(value) {
			Client.setVehicleHighGear(vehicle, value)
		}

	var isAlarmSet: Boolean
		get() {
			return Client.isVehicleAlarmSet(vehicle)
		}
		set(value) {
			Client.setVehicleAlarm(vehicle, value)
		}

	var alarmTimeLeft: Int
		get() {
			return Client.getVehicleAlarmTimeLeft(vehicle)
		}
		set(value) {
			Client.setVehicleAlarmTimeLeft(vehicle, value)
		}

	val dashboardSpeed: Double
		get() {
			return Client.getVehicleDashboardSpeed(vehicle)
		}

	var engineTemperature: Int
		get() {
			return Client.getVehicleEngineTemperature(vehicle)
		}
		set(value) {
			Client.setVehicleEngineTemperature(vehicle, value)
		}

	var fuelLevel: Number
		get() {
			return Client.getVehicleFuelLevel(vehicle)
		}
		set(value) {
			Client.setVehicleFuelLevel(vehicle, value)
		}

	//gravityAmount get set

	var handBrake: Boolean
		set(value) {
			Client.setVehicleHandbrake(vehicle, value)
		}
		get() {
			return Client.getVehicleHandbrake(vehicle)
		}

	//handlingField set get
	//handlingFloat set get
	//handlingInt set get
	//handlingVector set get
	//highGear set get
	//indicatorLights
	//nextGear

	val numberOfWheels: Int
		get() {
			return Client.getVehicleNumberOfWheels(vehicle)
		}

	val numberOfDoors: Int
		get() {
			return Client.getNumberOfVehicleDoors(vehicle)
		}

	//oilLevel вроде ни на что не влияет, поэтому использую как идентификатор
	//steeringAngle set get
	//steeringScale set get
	var turboPressure: Number
		get() {
			return Client.getVehicleTurboPressure(vehicle)
		}
		set(value) {
			Client.setVehicleTurboPressure(vehicle, value)
		}

	val isEngineStarting: Boolean
		get() {
			return Client.isVehicleEngineStarting(vehicle)
		}

	//isVehicleInteriorLightOn
	//isVehicleNeedsToBeHotwired
	//isVehiclePreviouslyOwnedByPlayer

	val isWanted: Boolean
		get() {
			return Client.isVehicleWanted(vehicle)
		}

	//	SetVehicleAutoRepairDisabled

	var dirtLevel: Int
		get() {
			return Client.getVehicleDirtLevel(vehicle)
		}
		set(value) {
			Client.setVehicleDirtLevel(vehicle, value)
		}

	init {
		if (Client.doesEntityExist(vehicle)) throw VehicleDoesntExistsException()

		wheels = mutableListOf()
		for (i in 0 until numberOfWheels) {
			wheels.add(Wheel(vehicle, i, tickExecutor))
		}

		doors = mutableListOf()
		for (i in 0 until numberOfDoors) {
			doors.add(Door(vehicle, i))
		}
	}

	fun setForwardSpeed(speed: Number) {
		Client.setVehicleForwardSpeed(vehicle, speed)
	}

//	fun setBoostActive(){
//		Client.setVehicleBoostActive(vehicle, true)
//		Client.setVehicleForwardSpeed(vehicle, 30)
//		Client.startScreenEffect(NativeScreenEffects.RACE_TURBO.effect, 0)
//	}

//	fun onTryEnter() {
//		//setvehicleundriveable
//	}

	fun destroy() {
		Client.setVehicleAsNoLongerNeeded(vehicle)
	}

	class Wheel(
		val index: Int,

		private val vehicle: Entity,
		private val tickExecutor: TickExecutorModule? = null
	) {

		var health: Number
			get() {
				return Client.getVehicleWheelHealth(vehicle, index)
			}
			set(value) {
				Client.setVehicleWheelHealth(vehicle, index, value)
			}

		val speed: Number
			get() {
				return Client.getVehicleWheelSpeed(vehicle, index)
			}

		var xOffset: Number
			get() {
				return Client.getVehicleWheelXOffset(vehicle, index)
			}
			set(value) {
				Client.setVehicleWheelXOffset(vehicle, index, value)

				if (tickExecutor == null) return
				tickExecutor.removeTick(xOffsetExecId)
				xOffsetExecId = tickExecutor.addTick { Client.setVehicleWheelXOffset(vehicle, index, value) }
			}

		var xRotation: Number
			get() {
				return Client.getVehicleWheelXrot(vehicle, index)
			}
			set(value) {
				Client.setVehicleWheelXrot(vehicle, index, value)

				if (tickExecutor == null) return
				tickExecutor.removeTick(xRotationExecId)
				xRotationExecId = tickExecutor.addTick { Client.setVehicleWheelXrot(vehicle, index, value) }
			}


		private var xOffsetExecId = -1
		private var xRotationExecId = -1
	}

	class Door(
		val index: Int,
		private val vehicle: Entity
	)

	companion object {
		fun create(
			id: Int,
			vehicleModel: NativeVehicles,
			coordinatesX: CoordinatesX,

			coroutineScope: CoroutineScope,
			tickExecutor: TickExecutorModule? = null
		): Deferred<Vehicle> {
			return coroutineScope.async {

				withTimeout(5_000) { Client.requestModel(vehicleModel.hash).join() }//todo test

				val vehicle = withTimeout(5_000) {
					online.fivem.client.gtav.Client.createVehicle(vehicleModel.hash, coordinatesX).await()
				}//todo test

				Client.setVehicleOilLevel(vehicle, id)

				Client.setVehicleOnGroundProperly(vehicle)
				Client.setModelAsNoLongerNeeded(vehicle)
				Client.setVehicleHasBeenOwnedByPlayer(vehicle)
				val networkId = Client.networkGetNetworkIdFromEntity(vehicle)

				Client.setNetworkIdCanMigrate(networkId, true)

				return@async Vehicle(vehicle, tickExecutor)
			}
		}
	}

	class VehicleDoesntExistsException : Exception()
}