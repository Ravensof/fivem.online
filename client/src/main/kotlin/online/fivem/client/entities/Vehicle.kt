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
import kotlin.reflect.KProperty

class Vehicle(
	val entity: Entity,

	tickExecutor: TickExecutorModule? = null
) {
	val id: Int = Client.getVehicleOilLevel(entity)?.toInt().orZero()

	val handling = Handling(entity)
	val wheels: List<Wheel>
	val doors: List<Door>
	val numberOfWheels = Client.getVehicleNumberOfWheels(entity)
	val numberOfDoors = Client.getNumberOfVehicleDoors(entity)
	val isEngineStarting: Boolean get() = Client.isVehicleEngineStarting(entity)
	val numberOfSeats = Client.getVehicleMaxNumberOfPassengers(entity) + 1
	val model = Client.getEntityModel(entity)
	val isOnAllWheels: Boolean get() = Client.isVehicleOnAllWheels(entity)

	var clutch: Number
		get() = Client.getVehicleClutch(entity)
		set(value) = Client.setVehicleClutch(entity, value)

	var currentRpm: Double
		set(value) = Client.setVehicleCurrentRpm(entity, value)
		get() = Client.getVehicleCurrentRpm(entity)

	var currentGear: Int
		get() = Client.getVehicleCurrentGear(entity)
		set(value) = Client.setVehicleCurrentGear(entity, value)

	var highGear: Int
		get() = Client.getVehicleHighGear(entity)
		set(value) = Client.setVehicleHighGear(entity, value)

	var isAlarmSet: Boolean
		get() = Client.isVehicleAlarmSet(entity)
		set(value) = Client.setVehicleAlarm(entity, value)

	var alarmTimeLeft: Int
		get() = Client.getVehicleAlarmTimeLeft(entity)
		set(value) = Client.setVehicleAlarmTimeLeft(entity, value)

	val dashboardSpeed: Double
		get() = Client.getVehicleDashboardSpeed(entity)

	var engineTemperature: Float
		get() = Client.getVehicleEngineTemperature(entity)
		set(value) = Client.setVehicleEngineTemperature(entity, value)

	var fuelLevel: Number
		get() = Client.getVehicleFuelLevel(entity)
		set(value) = Client.setVehicleFuelLevel(entity, value)

	//gravityAmount get set

	var isHandBrake: Boolean
		set(value) = Client.setVehicleHandbrake(entity, value)
		get() = Client.getVehicleHandbrake(entity)

	//handlingField set get
	//handlingFloat set get
	//handlingInt set get
	//handlingVector set get
	//indicatorLights
	var nextGear: Int
		get() = Client.getVehicleNextGear(entity)
		set(value) = Client.setVehicleNextGear(entity, value)

	//oilLevel вроде ни на что не влияет, поэтому использую как идентификатор
	//steeringAngle set get
	//steeringScale set get
	var turboPressure: Int
		get() = Client.getVehicleTurboPressure(entity).orZero()
		set(value) = Client.setVehicleTurboPressure(entity, value)

	var maxSpeed: Double
		get() = Client.getVehicleMaxSpeed(entity)
		set(value) = Client.setEntityMaxSpeed(entity, value)

	var isEngineOn: Boolean
		get() = Client.isVehicleEngineOn(entity)
		set(value) = Client.setVehicleEngineOn(entity, value, true)

	//isVehicleInteriorLightOn
	//isVehicleNeedsToBeHotwired
	//isVehiclePreviouslyOwnedByPlayer

	var isWanted: Boolean
		get() = Client.isVehicleWanted(entity)
		set(value) = Client.setVehicleIsWanted(entity, value)

	//	SetVehicleAutoRepairDisabled

	var dirtLevel: Int
		get() = Client.getVehicleDirtLevel(entity)
		set(value) = Client.setVehicleDirtLevel(entity, value)

	var engineHealth: Double
		get() = Client.getVehicleEngineHealth(entity)
		set(value) = Client.setVehicleEngineHealth(entity, value)

	var bodyHealth: Int
		get() = Client.getVehicleBodyHealth(entity)
		set(value) = Client.setVehicleBodyHealth(entity, value)

	var petrolTankHealth: Double
		get() = Client.getVehiclePetrolTankHealth(entity)
		set(value) = Client.setVehiclePetrolTankHealth(entity, value)

	var wheelType: Int
		get() = Client.getVehicleWheelType(entity)
		set(value) = Client.setVehicleWheelType(entity, value)

	var colours: Pair<Int, Int>
		get() = Client.getVehicleColours(entity)
		set(value) = Client.setVehicleColours(entity, value.first, value.second)

	var extraColors: Pair<Int, Int>
		get() = Client.getVehicleExtraColours(entity)
		set(value) = Client.setVehicleExtraColours(entity, value.first, value.second)

	var livery: Int?
		get() = Client.getVehicleLivery(entity)
		set(value) = Client.setVehicleLivery(entity, value ?: -1)

	var numberPlateText: String
		get() = Client.getVehicleNumberPlateText(entity).orEmpty()
		set(value) = Client.setVehicleNumberPlateText(entity, value)

	var numberPlateTextIndex: Int
		get() = Client.getVehicleNumberPlateTextIndex(entity)
		set(value) = Client.setVehicleNumberPlateTextIndex(entity, value)

	var windowTint: Int
		get() = Client.getVehicleWindowTint(entity)
		set(value) = Client.setVehicleWindowTint(entity, value)

	var neonLightsColour: RGB
		get() = Client.getVehicleNeonLightsColour(entity)
		set(value) = Client.setVehicleNeonLightsColour(entity, value)

	var tyreSmokeColor: RGB
		get() = Client.getVehicleTyreSmokeColor(entity)
		set(value) = Client.setVehicleTyreSmokeColor(entity, value)

	var brakeLights: Boolean = false
		set(value) {
			field = value
			Client.setVehicleBrakeLights(entity, value)
		}

	var tyresCanBurst: Boolean
		get() = Client.getVehicleTyresCanBurst(entity)
		set(value) = Client.setVehicleTyresCanBurst(entity, value)

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
//		Client.setVehicleBoostActive(entity, true)
//		Client.setVehicleForwardSpeed(entity, 30)
//		NativeScreenEffects.RACE_TURBO.start()
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
			get() = Client.getVehicleWheelHealth(vehicle, index)
			set(value) = Client.setVehicleWheelHealth(vehicle, index, value)

		val speed: Int = Client.getVehicleWheelSpeed(vehicle, index)

		var xOffset: Number
			get() = Client.getVehicleWheelXOffset(vehicle, index)
			set(value) {
				Client.setVehicleWheelXOffset(vehicle, index, value)

				if (tickExecutor == null) return
				tickExecutor.remove(xOffsetExecId)
				xOffsetExecId = tickExecutor.add { Client.setVehicleWheelXOffset(vehicle, index, value) }
			}

		var xRotation: Number
			get() = Client.getVehicleWheelXrot(vehicle, index)
			set(value) = Client.setVehicleWheelXrot(vehicle, index, value)

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

	class Handling(private val entity: Entity) {
		// https://gtamods.com/wiki/Handling.meta

		/**
		 * Multiplies the game's calculation of deformation-causing damage.
		 * Value: 0.0 - 10.0. 0.0 = no deformation through damage is possible. 10.0 = Ten times deformation-causing damage.
		 */
		var deformationDamageMultiplier: Double by HandlingDelegate("fDeformationDamageMult")

		/**
		 * Multiplies the game's calculation of deceleration. Bigger number = harder braking
		 * Brake Force formula TBD
		 * Value: 0.01 - 2.0 and above. 1.0 uses brake force calculation unmodified.
		 */
		var brakeForce: Double by HandlingDelegate("fBrakeForce")

		/**
		 * Multiplies the game's calculation of damage to the vehicle through weapon damage.
		 * Value: 0.0 - 10.0. 0.0 = no damage through weapons. 10.0 = Ten times damage through weapons.
		 */
		var weaponDamageMultiplier: Double by HandlingDelegate("fWeaponDamageMult")

		var initialDriveForce: Double by HandlingDelegate("fInitialDriveForce")

		/**
		 * How many forward speeds a transmission contains.
		 * Value: 1-16 or more.
		 */
		var initialDriveGears: Int by HandlingDelegate("nInitialDriveGears")

		private class HandlingDelegate(private val fieldName: String) {
			operator fun <T> getValue(handling: Handling, property: KProperty<*>): T = when (property.name[0]) {
				'f' -> Client.getVehicleHandlingFloat(handling.entity, "CHandlingData", fieldName).unsafeCast<T>()
				'i', 'n' -> Client.getVehicleHandlingInt(handling.entity, "CHandlingData", fieldName).unsafeCast<T>()
				else -> throw Exception("this type is not supported for vehicle handling")
			}

			operator fun <T> setValue(handling: Handling, property: KProperty<*>, value: T) {
				when (property.name[0]) {
					'f', 'n' -> Client.setVehicleHandlingFloat(
						handling.entity,
						"CHandlingData",
						fieldName,
						value as Double
					)
					'i' -> Client.setVehicleHandlingInt(handling.entity, "CHandlingData", fieldName, value as Int)
				}
			}
		}
	}

	class VehicleDoesntExistsException : Exception()
}