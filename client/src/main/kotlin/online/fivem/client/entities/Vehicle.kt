package online.fivem.client.entities

import kotlinx.coroutines.withTimeout
import online.fivem.Natives
import online.fivem.client.common.GlobalCache
import online.fivem.client.extensions.*
import online.fivem.common.common.EntityId
import online.fivem.common.common.Utils
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.RGB
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeVehicle
import online.fivem.common.gtav.NativeVehicleMod
import kotlin.reflect.KProperty

class Vehicle private constructor(
	entity: EntityId
) : Entity(entity) {

	val networkId = Natives.networkGetNetworkIdFromEntity(entity)
	val handling = Handling(entity)
	val wheels: List<Wheel>
	val doors: List<Door>
	val numberOfWheels = Natives.getVehicleNumberOfWheels(entity)
	val numberOfDoors = Natives.getNumberOfVehicleDoors(entity)
	val numberOfPassengersSeats = Natives.getVehicleMaxNumberOfPassengers(entity)
	val mod = Mod(entity)

	val classType: Int = Natives.getVehicleClass(entity)

	var modKit: Int
		get() = Natives.getVehicleModKit(entity)
		set(value) = Natives.setVehicleModKit(entity, value)

	var ownedByPlayer: Boolean
		get() = Natives.isVehiclePreviouslyOwnedByPlayer(entity)
		set(value) = Natives.setVehicleHasBeenOwnedByPlayer(entity, value)

	var clutch: Number
		get() = Natives.getVehicleClutch(entity)
		set(value) = Natives.setVehicleClutch(entity, value)

	var currentRpm: Double
		set(value) = Natives.setVehicleCurrentRpm(entity, value)
		get() = Natives.getVehicleCurrentRpm(entity)

	var currentGear: Int
		get() = Natives.getVehicleCurrentGear(entity)
		set(value) = Natives.setVehicleCurrentGear(entity, value)

	var highGear: Int
		get() = Natives.getVehicleHighGear(entity)
		set(value) = Natives.setVehicleHighGear(entity, value)

	var isAlarmSet: Boolean
		get() = Natives.isVehicleAlarmSet(entity)
		set(value) = Natives.setVehicleAlarm(entity, value)

	var alarmTimeLeft: Int
		get() = Natives.getVehicleAlarmTimeLeft(entity)
		set(value) = Natives.setVehicleAlarmTimeLeft(entity, value)

	val dashboardSpeed: Double
		get() = Natives.getVehicleDashboardSpeed(entity)

	var engineTemperature: Float
		get() = Natives.getVehicleEngineTemperature(entity)
		set(value) = Natives.setVehicleEngineTemperature(entity, value)

	var fuelLevel: Double
		get() = Natives.getVehicleFuelLevel(entity)
		set(value) = Natives.setVehicleFuelLevel(entity, value)

	var oilLevel: Float
		get() = Natives.getVehicleOilLevel(entity)
		set(value) = Natives.setVehicleOilLevel(entity, value)

	//gravityAmount get set

	var isHandBrake: Boolean
		set(value) = Natives.setVehicleHandbrake(entity, value)
		get() = Natives.getVehicleHandbrake(entity)

	//handlingField set get
	//handlingFloat set get
	//handlingInt set get
	//handlingVector set get
	//indicatorLights
	var nextGear: Int
		get() = Natives.getVehicleNextGear(entity)
		set(value) = Natives.setVehicleNextGear(entity, value)

	//steeringAngle set get
	//steeringScale set get
	var turboPressure: Int
		get() = Natives.getVehicleTurboPressure(entity).orZero()
		set(value) = Natives.setVehicleTurboPressure(entity, value)

	var maxSpeed: Double
		get() = Natives.getVehicleMaxSpeed(entity)
		set(value) = Natives.setEntityMaxSpeed(entity, value)

	var isEngineOn: Boolean
		get() = Natives.isVehicleEngineOn(entity)
		set(value) = Natives.setVehicleEngineOn(entity, value, true)

	//isVehicleInteriorLightOn
	//isVehicleNeedsToBeHotwired
	//isVehiclePreviouslyOwnedByPlayer

	var isWanted: Boolean
		get() = Natives.isVehicleWanted(entity)
		set(value) = Natives.setVehicleIsWanted(entity, value)

	//	SetVehicleAutoRepairDisabled

	var dirtLevel: Int
		get() = Natives.getVehicleDirtLevel(entity)
		set(value) = Natives.setVehicleDirtLevel(entity, value)

	var engineHealth: Double
		get() = Natives.getVehicleEngineHealth(entity)
		set(value) = Natives.setVehicleEngineHealth(entity, value)

	var bodyHealth: Int
		get() = Natives.getVehicleBodyHealth(entity)
		set(value) = Natives.setVehicleBodyHealth(entity, value)

	var petrolTankHealth: Double
		get() = Natives.getVehiclePetrolTankHealth(entity)
		set(value) = Natives.setVehiclePetrolTankHealth(entity, value)

	var wheelType: Int
		get() = Natives.getVehicleWheelType(entity)
		set(value) = Natives.setVehicleWheelType(entity, value)

	var colors: Pair<Int, Int>
		get() = Natives.getVehicleColours(entity)
		set(value) = Natives.setVehicleColours(entity, value.first, value.second)

	var extraColors: Pair<Int, Int>
		get() = Natives.getVehicleExtraColours(entity)
		set(value) = Natives.setVehicleExtraColours(entity, value.first, value.second)

	var livery: Int?
		get() = Natives.getVehicleLivery(entity)
		set(value) = Natives.setVehicleLivery(entity, value ?: -1)

	var numberPlateText: String
		get() = Natives.getVehicleNumberPlateText(entity).orEmpty()
		set(value) = Natives.setVehicleNumberPlateText(entity, value)

	var numberPlateTextIndex: Int
		get() = Natives.getVehicleNumberPlateTextIndex(entity)
		set(value) = Natives.setVehicleNumberPlateTextIndex(entity, value)

	var windowTint: Int
		get() = Natives.getVehicleWindowTint(entity)
		set(value) = Natives.setVehicleWindowTint(entity, value)

	var neonLightsColour: RGB
		get() = Natives.getVehicleNeonLightsColour(entity)
		set(value) = Natives.setVehicleNeonLightsColour(entity, value)

	var tyreSmokeColor: RGB
		get() = Natives.getVehicleTyreSmokeColor(entity)
		set(value) = Natives.setVehicleTyreSmokeColor(entity, value)

	var tyresCanBurst: Boolean
		get() = Natives.getVehicleTyresCanBurst(entity)
		set(value) = Natives.setVehicleTyresCanBurst(entity, value)

	var doorsLockStatus: Int
		get() = Natives.getVehicleDoorLockStatus(entity)
		set(value) = Natives.setVehicleDoorsLocked(entity, value)

	init {
		if (!Natives.doesEntityExist(entity)) throw VehicleDoesntExistsException("vehicle $entity doesnt exists")

		Natives.setNetworkIdCanMigrate(networkId, true)

		modKit = 0

		wheels = mutableListOf()
		for (i in 0 until numberOfWheels) {
			wheels.add(Wheel(i, entity))
		}

		doors = mutableListOf()
		for (i in 0 until numberOfDoors) {
			doors.add(Door(i, entity))
		}
	}

	override fun equals(other: Any?): Boolean {
		return super.equals(other) && other is Vehicle
				&& networkId == other.networkId
				&& numberOfWheels == other.numberOfWheels
				&& numberOfDoors == other.numberOfDoors
				&& numberOfPassengersSeats == other.numberOfPassengersSeats
				&& classType == other.classType
	}

	override fun hashCode(): Int {
		var result = super.hashCode()
		result = 31 * result + networkId
		result = 31 * result + numberOfWheels
		result = 31 * result + numberOfDoors
		result = 31 * result + numberOfPassengersSeats
		result = 31 * result + classType
		return result
	}

	fun setUndriveable(undriveable: Boolean = false) {
		Natives.setVehicleUndriveable(entity, undriveable)
	}

	fun setEngineTorqueMultiplier(value: Double) {
		Natives.setVehicleEngineTorqueMultiplier(entity, value)
	}

	fun setBrakeLights(value: Boolean = false) {
		Natives.setVehicleBrakeLights(entity, value)
	}

	fun addEnginePowerMultiplier(percents: Double) {
		Natives.setVehicleEnginePowerMultiplier(entity, percents)
	}

	fun setForwardSpeed(speed: Number) {
		Natives.setVehicleForwardSpeed(entity, speed)
	}

	fun setOnGroundProperly() {
		Natives.setVehicleOnGroundProperly(entity)
	}

	fun getPassengers(): List<Ped> {
		val list = mutableListOf<Ped>()

		for (i in -1 until numberOfPassengersSeats) {
			val entity = Natives.getPedInVehicleSeat(entity, i) ?: continue
			list.add(Ped.newInstance(entity))
		}

		return list
	}

	fun isEngineStarting() = Natives.isVehicleEngineStarting(entity)

	fun isEngineRunning() = Natives.getIsVehicleEngineRunning(entity)

	fun isOnAllWheels() = Natives.isVehicleOnAllWheels(entity)

	fun turnEngineOn(value: Boolean, instantly: Boolean = false) {
		Natives.setVehicleEngineOn(entity, value, instantly)
	}

	fun getTurboPressureRPMBased(startRPM: Double = 0.6, endRPM: Double = 1.0): Double {
		return (
				Utils.normalizeToLimits(
					currentRpm, startRPM, endRPM
				) - startRPM

				) / (endRPM - startRPM)
	}

//	fun setBoostActive(){
//		Natives.setVehicleBoostActive(entity, true)
//		Natives.setVehicleForwardSpeed(entity, 30)
//		NativeScreenEffects.RACE_TURBO.start()
//	}

//	fun onTryEnter() {
//		//setvehicleundriveable
//	}

	fun destroy() {
		Natives.setNetworkIdExistsOnAllMachines(networkId, false)
		Natives.setVehicleAsNoLongerNeeded(entity)
	}

	class Mod(private val vehicle: EntityId) {
		operator fun get(mod: NativeVehicleMod): Int {
			return mod.getOn(vehicle) ?: -1
		}

		operator fun set(mod: NativeVehicleMod, value: Int) {
			mod.setOn(vehicle, value)
		}
	}

	class Wheel(
		val index: Int,

		private val vehicle: EntityId
	) {

		var health: Number
			get() = Natives.getVehicleWheelHealth(vehicle, index)
			set(value) = Natives.setVehicleWheelHealth(vehicle, index, value)

		val speed: Int = Natives.getVehicleWheelSpeed(vehicle, index)

		var xOffset: Number
			get() = Natives.getVehicleWheelXOffset(vehicle, index)
			set(value) = Natives.setVehicleWheelXOffset(vehicle, index, value)

		var xRotation: Number
			get() = Natives.getVehicleWheelXrot(vehicle, index)
			set(value) = Natives.setVehicleWheelXrot(vehicle, index, value)

		fun burst(onRim: Boolean, damage: Double) {
			Natives.setVehicleTyreBurst(vehicle, index, onRim, damage)
		}
	}

	class Door(
		val index: Int,
		private val vehicle: EntityId
	)

	companion object {

		suspend fun create(
			vehicleModel: NativeVehicle,
			coordinatesX: CoordinatesX
		) = create(vehicleModel.hash, coordinatesX)

		suspend fun create(
			vehicleModel: Int,
			coordinatesX: CoordinatesX
		): Vehicle {

			val entity = withTimeout(5_000) {
				Natives.createVehicle(vehicleModel, coordinatesX)
			}

			return newInstance(entity).apply {
				Natives.setNetworkIdExistsOnAllMachines(networkId, true)
				ownedByPlayer = true
				setOnGroundProperly()
			}
		}

		fun newInstance(
			entity: EntityId
		): Vehicle {

			GlobalCache.getVehicle(entity)?.let {
				return it
			}

			val vehicle = Vehicle(entity)

			GlobalCache.putVehicle(vehicle)

			return vehicle
		}
	}

	/**
	 * https://gtamods.com/wiki/Handling.meta for more params
	 */
	@Suppress("unused")
	class Handling(private val entity: EntityId) {

		/**
		 * This is the weight of the vehicle in kilograms. Only used when the vehicle collides with another vehicle or non-static object.
		 * Value: 0.0 - 10000.0 and above.
		 */
		var mass: Double by HandlingDelegate("fMass")

		/**
		 * This value is used to determine whether a vehicle is front, rear, or four wheel drive.
		 * Value: 0.0 is rear wheel drive, 1.0 is front wheel drive, and any value between 0.01 and 0.99 is four wheel
		 * drive (0.5 give both front and rear axles equal force, being perfect 4WD.)
		 */
		var driveBiasFront: Double by HandlingDelegate("fDriveBiasFront")

		/**
		 * How many forward speeds a transmission contains.
		 * Value: 1-16 or more.
		 */
		var initialDriveGears: Int by HandlingDelegate("nInitialDriveGears")

		/**
		 * This multiplier modifies the game's calculation of drive force (from the output of the transmission).
		 * Drive force formula TBD
		 * Value: 0.01 - 2.0 and above. 1.0 uses drive force calculation unmodified. Values less than 1.0 will in effect
		 * give the vehicle less drive force and values greater than 1.0 will produce more drive force. Most cars have between 0.10 and 0.25.
		 */
		var initialDriveForce: Double by HandlingDelegate("fInitialDriveForce")

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
		 * Multiplies the game's calculation of damage to the vehicle through collision.
		 * Value: 0.0 - 10.0. 0.0 = no damage through collision. 10.0 = Ten times damage through collision.
		 */
		var collisionDamageMultiplier: Double by HandlingDelegate("fCollisionDamageMult")

		/**
		 * Multiplies the game's calculation of damage to the vehicle through weapon damage.
		 * Value: 0.0 - 10.0. 0.0 = no damage through weapons. 10.0 = Ten times damage through weapons.
		 */
		var weaponDamageMultiplier: Double by HandlingDelegate("fWeaponDamageMult")

		/**
		 * Multiplies the game's calculation of damage to the engine, causing explosion or engine failure.
		 * Value: 0.0 - 10.0. 0.0 = no damage to the engine. 10.0 = Ten times damage to the engine.
		 */
		var engineDamageMultiplier: Double by HandlingDelegate("fEngineDamageMult")


		private class HandlingDelegate(private val fieldName: String) {
			operator fun <T> getValue(handling: Handling, property: KProperty<*>): T = when (fieldName[0]) {
				'f' -> Natives.getVehicleHandlingFloat(handling.entity, "CHandlingData", fieldName).unsafeCast<T>()
				'i', 'n' -> Natives.getVehicleHandlingInt(handling.entity, "CHandlingData", fieldName).unsafeCast<T>()
				else -> throw Exception("this type ($fieldName) is not supported for vehicle handling")
			}

			operator fun <T> setValue(handling: Handling, property: KProperty<*>, value: T) {
				when (property.name[0]) {
					'f', 'n' -> Natives.setVehicleHandlingFloat(
						handling.entity,
						"CHandlingData",
						fieldName,
						value as Double
					)
					'i' -> Natives.setVehicleHandlingInt(handling.entity, "CHandlingData", fieldName, value as Int)
				}
			}
		}
	}

	class VehicleDoesntExistsException(message: String) : Exception(message)
}