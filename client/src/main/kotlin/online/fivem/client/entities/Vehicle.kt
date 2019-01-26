package online.fivem.client.entities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import online.fivem.client.extensions.createVehicle
import online.fivem.client.extensions.setVehicleNeonLightsColour
import online.fivem.client.extensions.setVehicleTyreSmokeColor
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.common.Entity
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.RGB
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

	var wheelType: Int
		get() {
			return Client.getVehicleWheelType(entity)
		}
		set(value) {
			Client.setVehicleWheelType(entity, value)
		}

	var colours: Pair<Int, Int>
		get() {
			return Client.getVehicleColours(entity)
		}
		set(value) {
			Client.setVehicleColours(entity, value.first, value.second)
		}

	var extraColors: Pair<Int, Int>
		get() {
			return Client.getVehicleExtraColours(entity)
		}
		set(value) {
			Client.setVehicleExtraColours(entity, value.first, value.second)
		}

	val model = Client.getEntityModel(entity)

	var livery: Int?
		get() {
			return Client.getVehicleLivery(entity)
		}
		set(value) {
			Client.setVehicleLivery(entity, value ?: -1)
		}

	var numberPlateText: String
		get() {
			return Client.getVehicleNumberPlateText(entity).orEmpty()
		}
		set(value) {
			Client.setVehicleNumberPlateText(entity, value)
		}

	var numberPlateTextIndex: Int
		get() {
			return Client.getVehicleNumberPlateTextIndex(entity)
		}
		set(value) {
			Client.setVehicleNumberPlateTextIndex(entity, value)
		}

	var windowTint: Int
		get() {
			return Client.getVehicleWindowTint(entity)
		}
		set(value) {
			Client.setVehicleWindowTint(entity, value)
		}

	var neonLightsColour: RGB
		get() {
			return Client.getVehicleNeonLightsColour(entity)
		}
		set(value) {
			Client.setVehicleNeonLightsColour(entity, value)
		}

	var tyreSmokeColor: RGB
		get() {
			return Client.getVehicleTyreSmokeColor(entity)
		}
		set(value) {
			Client.setVehicleTyreSmokeColor(entity, value)
		}

	val isOnAllWheels: Boolean
		get() {
			return Client.isVehicleOnAllWheels(entity)
		}

	var brakeLights: Boolean = false
		set(value) {
			field = value
			Client.setVehicleBrakeLights(entity, value)
		}

	var tyresCanBurst: Boolean
		get() {
			return Client.getVehicleTyresCanBurst(entity)
		}
		set(value) {
			Client.setVehicleTyresCanBurst(entity, value)
		}

	//todo mod, neon

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

		val speed: Int
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
			}


		private var xOffsetExecId = -1
		private var xRotationExecId = -1
	}

	class Door(
		val index: Int,
		private val vehicle: Entity
	)

	companion object {
//		fun getProperties(vehicle: Entity): Any {
//
//			val color1: Int
//			val color2: Int
//
//			Client.getVehicleColours(vehicle).let {
//				color1 = it.first
//				color2 = it.second
//			}
//
//			val pearlescentColor: Int
//			val wheelColor: Int
//
//			Client.getVehicleExtraColours(vehicle).let {
//				pearlescentColor = it.first
//				wheelColor = it.second
//			}
//
//			return object {
//
//				val model = Client.getEntityModel(vehicle)
//
//				val plate = Client.getVehicleNumberPlateText(vehicle)
//				val plateIndex = Client.getVehicleNumberPlateTextIndex(vehicle)
//
//				val health = Client.getEntityHealth(vehicle)
//				val dirtLevel = Client.getVehicleDirtLevel(vehicle)
//
//				val color1 = color1
//				val color2 = color2
//
//				val pearlescentColor = pearlescentColor
//				val wheelColor = wheelColor
//
//				val wheels = Client.getVehicleWheelType(vehicle)
//				val windowTint = Client.getVehicleWindowTint(vehicle)
//
//				val neonEnabled = {
//					Client.isVehicleNeonLightEnabled(vehicle, 0)
//					Client.isVehicleNeonLightEnabled(vehicle, 1)
//					Client.isVehicleNeonLightEnabled(vehicle, 2)
//					Client.isVehicleNeonLightEnabled(vehicle, 3)
//				}
//
//				val extras = {
//
//				}
//
//				val neonColor = Client.getVehicleNeonLightsColour(vehicle)
//				val tyreSmokeColor = Client.getVehicleTyreSmokeColor(vehicle)
//
//				val modSpoilers = Client.getVehicleMod(vehicle, 0)
//				val modFrontBumper = Client.getVehicleMod(vehicle, 1)
//				val modRearBumper = Client.getVehicleMod(vehicle, 2)
//				val modSideSkirt = Client.getVehicleMod(vehicle, 3)
//				val modExhaust = Client.getVehicleMod(vehicle, 4)
//				val modFrame = Client.getVehicleMod(vehicle, 5)
//				val modGrille = Client.getVehicleMod(vehicle, 6)
//				val modHood = Client.getVehicleMod(vehicle, 7)
//				val modFender = Client.getVehicleMod(vehicle, 8)
//				val modRightFender = Client.getVehicleMod(vehicle, 9)
//				val modRoof = Client.getVehicleMod(vehicle, 10)
//
//				val modEngine = Client.getVehicleMod(vehicle, 11)
//				val modBrakes = Client.getVehicleMod(vehicle, 12)
//				val modTransmission = Client.getVehicleMod(vehicle, 13)
//				val modHorns = Client.getVehicleMod(vehicle, 14)
//				val modSuspension = Client.getVehicleMod(vehicle, 15)
//				val modArmor = Client.getVehicleMod(vehicle, 16)
//
//				val modTurbo = Client.isToggleModOn(vehicle, 18)
//				val modSmokeEnabled = Client.isToggleModOn(vehicle, 20)
//				val modXenon = Client.isToggleModOn(vehicle, 22)
//
//				val modFrontWheels = Client.getVehicleMod(vehicle, 23)
//				val modBackWheels = Client.getVehicleMod(vehicle, 24)
//
//				val modPlateHolder = Client.getVehicleMod(vehicle, 25)
//				val modVanityPlate = Client.getVehicleMod(vehicle, 26)
//				val modTrimA = Client.getVehicleMod(vehicle, 27)
//				val modOrnaments = Client.getVehicleMod(vehicle, 28)
//				val modDashboard = Client.getVehicleMod(vehicle, 29)
//				val modDial = Client.getVehicleMod(vehicle, 30)
//				val modDoorSpeaker = Client.getVehicleMod(vehicle, 31)
//				val modSeats = Client.getVehicleMod(vehicle, 32)
//				val modSteeringWheel = Client.getVehicleMod(vehicle, 33)
//				val modShifterLeavers = Client.getVehicleMod(vehicle, 34)
//				val modAPlate = Client.getVehicleMod(vehicle, 35)
//				val modSpeakers = Client.getVehicleMod(vehicle, 36)
//				val modTrunk = Client.getVehicleMod(vehicle, 37)
//				val modHydrolic = Client.getVehicleMod(vehicle, 38)
//				val modEngineBlock = Client.getVehicleMod(vehicle, 39)
//				val modAirFilter = Client.getVehicleMod(vehicle, 40)
//				val modStruts = Client.getVehicleMod(vehicle, 41)
//				val modArchCover = Client.getVehicleMod(vehicle, 42)
//				val modAerials = Client.getVehicleMod(vehicle, 43)
//				val modTrimB = Client.getVehicleMod(vehicle, 44)
//				val modTank = Client.getVehicleMod(vehicle, 45)
//				val modWindows = Client.getVehicleMod(vehicle, 46)
//				val modLivery = Client.getVehicleLivery(vehicle)
//			}
//		}

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