package online.fivem.client.entities

import kotlinx.coroutines.withTimeout
import online.fivem.client.common.GlobalCache
import online.fivem.client.extensions.*
import online.fivem.client.gtav.Client
import online.fivem.common.common.EntityId
import online.fivem.common.common.Utils
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.RGB
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeVehicles
import kotlin.reflect.KProperty

class Vehicle private constructor(
	entity: EntityId
) : Entity(entity) {

	val networkId = Client.networkGetNetworkIdFromEntity(entity)
	val handling = Handling(entity)
	val wheels: List<Wheel>
	val doors: List<Door>
	val numberOfWheels = Client.getVehicleNumberOfWheels(entity)
	val numberOfDoors = Client.getNumberOfVehicleDoors(entity)
	val numberOfPassengersSeats = Client.getVehicleMaxNumberOfPassengers(entity)
	val mod = Mod(entity)

	val classType: Int = Client.getVehicleClass(entity)

	var modKit: Int
		get() = Client.getVehicleModKit(entity)
		set(value) = Client.setVehicleModKit(entity, value)

	var ownedByPlayer: Boolean
		get() = Client.isVehiclePreviouslyOwnedByPlayer(entity)
		set(value) = Client.setVehicleHasBeenOwnedByPlayer(entity, value)

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

	var fuelLevel: Double
		get() = Client.getVehicleFuelLevel(entity)
		set(value) = Client.setVehicleFuelLevel(entity, value)

	var oilLevel: Float
		get() = Client.getVehicleOilLevel(entity)
		set(value) = Client.setVehicleOilLevel(entity, value)

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

	var colors: Pair<Int, Int>
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

	var tyresCanBurst: Boolean
		get() = Client.getVehicleTyresCanBurst(entity)
		set(value) = Client.setVehicleTyresCanBurst(entity, value)

	var doorsLockStatus: Int
		get() = Client.getVehicleDoorLockStatus(entity)
		set(value) = Client.setVehicleDoorsLocked(entity, value)

	init {
		if (!Client.doesEntityExist(entity)) throw VehicleDoesntExistsException("vehicle $entity doesnt exists")

		Client.setNetworkIdCanMigrate(networkId, true)

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
		return other is Vehicle && other.entity == entity
	}

	fun setUndriveable(undriveable: Boolean = false) {
		Client.setVehicleUndriveable(entity, undriveable)
	}

	fun setEngineTorqueMultiplier(value: Double) {
		Client.setVehicleEngineTorqueMultiplier(entity, value)
	}

	fun setBrakeLights(value: Boolean = false) {
		Client.setVehicleBrakeLights(entity, value)
	}

	fun addEnginePowerMultiplier(percents: Double) {
		Client.setVehicleEnginePowerMultiplier(entity, percents)
	}

	fun setForwardSpeed(speed: Number) {
		Client.setVehicleForwardSpeed(entity, speed)
	}

	fun setOnGroundProperly() {
		Client.setVehicleOnGroundProperly(entity)
	}

	fun getPassengers(): List<Ped> {
		val list = mutableListOf<Ped>()

		for (i in -1 until numberOfPassengersSeats) {
			val entity = Client.getPedInVehicleSeat(entity, i) ?: continue
			list.add(Ped.newInstance(entity))
		}

		return list
	}

	fun isEngineStarting() = Client.isVehicleEngineStarting(entity)

	fun isEngineRunning() = Client.getIsVehicleEngineRunning(entity)

	fun isOnAllWheels() = Client.isVehicleOnAllWheels(entity)

	fun turnEngineOn(value: Boolean, instantly: Boolean = false) {
		Client.setVehicleEngineOn(entity, value, instantly)
	}

	fun getTurboPressureRPMBased(vehicle: EntityId, startRPM: Double = 0.6, endRPM: Double = 1.0): Double {
		return (
				Utils.normalizeToLimits(
					currentRpm, startRPM, endRPM
				) - startRPM

				) / (endRPM - startRPM)
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

	class Mod(private val vehicle: EntityId) {
		operator fun get(mod: NativeVehicles.Mod): Int {
			return mod.getOn(vehicle) ?: -1
		}

		operator fun set(mod: NativeVehicles.Mod, value: Int) {
			mod.setOn(vehicle, value)
		}
	}

	class Wheel(
		val index: Int,

		private val vehicle: EntityId
	) {

		var health: Number
			get() = Client.getVehicleWheelHealth(vehicle, index)
			set(value) = Client.setVehicleWheelHealth(vehicle, index, value)

		val speed: Int = Client.getVehicleWheelSpeed(vehicle, index)

		var xOffset: Number
			get() = Client.getVehicleWheelXOffset(vehicle, index)
			set(value) = Client.setVehicleWheelXOffset(vehicle, index, value)

		var xRotation: Number
			get() = Client.getVehicleWheelXrot(vehicle, index)
			set(value) = Client.setVehicleWheelXrot(vehicle, index, value)

		fun burst(onRim: Boolean, damage: Double) {
			Client.setVehicleTyreBurst(vehicle, index, onRim, damage)
		}
	}

	class Door(
		val index: Int,
		private val vehicle: EntityId
	)

	companion object {

		suspend fun create(
			vehicleModel: NativeVehicles,
			coordinatesX: CoordinatesX
		): Vehicle {

			val entity = withTimeout(5_000) {
				Client.createVehicle(vehicleModel.hash, coordinatesX)
			}//todo test

			return newInstance(entity).apply {
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
				'f' -> Client.getVehicleHandlingFloat(handling.entity, "CHandlingData", fieldName).unsafeCast<T>()
				'i', 'n' -> Client.getVehicleHandlingInt(handling.entity, "CHandlingData", fieldName).unsafeCast<T>()
				else -> throw Exception("this type ($fieldName) is not supported for vehicle handling")
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

	class VehicleDoesntExistsException(message: String) : Exception(message)
}