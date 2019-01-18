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
	val entity: Entity,

	tickExecutor: TickExecutorModule? = null
) {
	val id: Int = Client.getVehicleOilLevel(entity)?.toInt().orZero()

	val wheels: List<Wheel>
	val doors: List<Door>

	var clutch: Number
		get() {
			return Client.getVehicleClutch(entity)
		}
		set(value) {
			Client.setVehicleClutch(entity, value)
		}

	var currentRpm: Double
		set(value) {
			Client.setVehicleCurrentRpm(entity, value)
		}
		get() {
			return Client.getVehicleCurrentRpm(entity)
		}

	val currentGear: Int
		get() {
			return Client.getVehicleCurrentGear(entity)
		}

	var highGear: Int
		get() {
			return Client.getVehicleHighGear(entity)
		}
		set(value) {
			Client.setVehicleHighGear(entity, value)
		}

	var isAlarmSet: Boolean
		get() {
			return Client.isVehicleAlarmSet(entity)
		}
		set(value) {
			Client.setVehicleAlarm(entity, value)
		}

	var alarmTimeLeft: Int
		get() {
			return Client.getVehicleAlarmTimeLeft(entity)
		}
		set(value) {
			Client.setVehicleAlarmTimeLeft(entity, value)
		}

	val dashboardSpeed: Double
		get() {
			return Client.getVehicleDashboardSpeed(entity)
		}

	var engineTemperature: Int
		get() {
			return Client.getVehicleEngineTemperature(entity)
		}
		set(value) {
			Client.setVehicleEngineTemperature(entity, value)
		}

	var fuelLevel: Number
		get() {
			return Client.getVehicleFuelLevel(entity)
		}
		set(value) {
			Client.setVehicleFuelLevel(entity, value)
		}

	//gravityAmount get set

	var isHandBrake: Boolean
		set(value) {
			Client.setVehicleHandbrake(entity, value)
		}
		get() {
			return Client.getVehicleHandbrake(entity)
		}

	//handlingField set get
	//handlingFloat set get
	//handlingInt set get
	//handlingVector set get
	//highGear set get
	//indicatorLights
	//nextGear

	val numberOfWheels = Client.getVehicleNumberOfWheels(entity)

	val numberOfDoors = Client.getNumberOfVehicleDoors(entity)

	//oilLevel вроде ни на что не влияет, поэтому использую как идентификатор
	//steeringAngle set get
	//steeringScale set get
	var turboPressure: Number
		get() {
			return Client.getVehicleTurboPressure(entity).orZero()
		}
		set(value) {
			Client.setVehicleTurboPressure(entity, value)
		}

	var maxSpeed: Double
		get() {
			return Client.getVehicleMaxSpeed(entity)
		}
		set(value) {
			Client.setEntityMaxSpeed(entity, value)
		}

	val isEngineStarting: Boolean
		get() {
			return Client.isVehicleEngineStarting(entity)
		}

	var isEngineOn: Boolean
		get() {
			return Client.isVehicleEngineOn(entity)
		}
		set(value) {
			Client.setVehicleEngineOn(entity, value, true)
		}

	//isVehicleInteriorLightOn
	//isVehicleNeedsToBeHotwired
	//isVehiclePreviouslyOwnedByPlayer

	var isWanted: Boolean
		get() {
			return Client.isVehicleWanted(entity)
		}
		set(value) {
			Client.setVehicleIsWanted(entity, value)
		}

	//	SetVehicleAutoRepairDisabled

	var dirtLevel: Int
		get() {
			return Client.getVehicleDirtLevel(entity)
		}
		set(value) {
			Client.setVehicleDirtLevel(entity, value)
		}

	var engineHealth: Double
		get() {
			return Client.getVehicleEngineHealth(entity)
		}
		set(value) {
			Client.setVehicleEngineHealth(entity, value)
		}

	var bodyHealth: Int
		get() {
			return Client.getVehicleBodyHealth(entity)
		}
		set(value) {
			Client.setVehicleBodyHealth(entity, value)
		}

	var petrolTankHealth: Double
		get() {
			return Client.getVehiclePetrolTankHealth(entity)
		}
		set(value) {
			Client.setVehiclePetrolTankHealth(entity, value)
		}

	val numberOfSeats = Client.getVehicleMaxNumberOfPassengers(entity) + 1

	init {
		if (Client.doesEntityExist(entity)) throw VehicleDoesntExistsException()

		wheels = mutableListOf()
		for (i in 0 until numberOfWheels) {
			wheels.add(Wheel(entity, i, tickExecutor))
		}

		doors = mutableListOf()
		for (i in 0 until numberOfDoors) {
			doors.add(Door(entity, i))
		}
	}

	fun addEnginePowerMultiplier(percents: Double) {
		Client.setVehicleEnginePowerMultiplier(entity, percents)
	}

	fun setForwardSpeed(speed: Number) {
		Client.setVehicleForwardSpeed(entity, speed)
	}

	fun getPassengers(): MutableList<Entity> {
		val list = mutableListOf<Entity>()

		for (i in -1 until Client.getVehicleMaxNumberOfPassengers(entity)) {
			val entity = Client.getPedInVehicleSeat(entity, -1) ?: continue
			list.add(entity)
		}

		return list
	}

	fun turnEngineOn(value: Boolean) {
		Client.setVehicleEngineOn(entity, value, false)
	}

	fun isOnAllWheels(): Boolean {
		return Client.isVehicleOnAllWheels(entity)
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
		Client.setVehicleAsNoLongerNeeded(entity)
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
				tickExecutor.remove(xOffsetExecId)
				xOffsetExecId = tickExecutor.add { Client.setVehicleWheelXOffset(vehicle, index, value) }
			}

		var xRotation: Number
			get() {
				return Client.getVehicleWheelXrot(vehicle, index)
			}
			set(value) {
				Client.setVehicleWheelXrot(vehicle, index, value)

				if (tickExecutor == null) return
				tickExecutor.remove(xRotationExecId)
				xRotationExecId = tickExecutor.add { Client.setVehicleWheelXrot(vehicle, index, value) }
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

		fun fromEntity(list: List<Entity>, tickExecutor: TickExecutorModule? = null): List<Vehicle> {
			return list.map {
				Vehicle(
					entity = it,
					tickExecutor = tickExecutor
				)
			}
		}
	}

	class VehicleDoesntExistsException : Exception()
}