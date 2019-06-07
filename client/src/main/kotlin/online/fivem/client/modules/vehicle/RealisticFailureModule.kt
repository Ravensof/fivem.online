package online.fivem.client.modules.vehicle

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.entities.Vehicle
import online.fivem.client.events.PlayerLeftOrJoinVehicleEvent
import online.fivem.client.extensions.disableControlAction
import online.fivem.client.extensions.getDisabledControlNormal
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.common.Event
import online.fivem.common.common.generateLong
import online.fivem.common.gtav.NativeControls
import kotlin.math.pow
import kotlin.random.Random

//todo переписать
class RealisticFailureModule(
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	private var lastVehicle: Vehicle? = null
	private var vehicleClass = 0
	private var fCollisionDamageMult = 0.0
	private var fDeformationDamageMult = 0.0
	private var fEngineDamageMult = 0.0
	private var fBrakeForce = 1.0
	private var isBrakingForward = false
	private var isBrakingReverse = false

	private var healthEngineLast = 1000.0
	private var healthEngineCurrent = 1000.0
	private var healthEngineNew = 1000.0
	private var healthEngineDelta = 0.0
	private var healthEngineDeltaScaled = 0.0

	private var healthBodyLast = 1000
	private var healthBodyCurrent = 1000
	private var healthBodyNew = 1000
	private var healthBodyDelta = 0
	private var healthBodyDeltaScaled = 0.0

	private var healthPetrolTankLast = 1000.0
	private var healthPetrolTankCurrent = 1000.0
	private var healthPetrolTankNew = 1000.0
	private var healthPetrolTankDelta = 0.0
	private var healthPetrolTankDeltaScaled = 0.0

	// the tire burst lottery runs roughly 1200 times per minute
	private var tireBurstMaxNumber = Cfg.randomTireBurstInterval * 1200

	// If we hit this number again randomly, a tire will burst.
	private var tireBurstLuckyNumber: Int =
		if (Cfg.randomTireBurstInterval != 0) Random.nextInt(tireBurstMaxNumber) else 0

	private var mainJob: Job? = null
	private val tickHandle = generateLong()


	override fun onStart() = launch {
		tickExecutorModule.waitForStart()

		Event.apply {
			on<PlayerLeftOrJoinVehicleEvent.Join.Driver> { onJoinVehicle(it.vehicle) }
			on<PlayerLeftOrJoinVehicleEvent.Changed> { onChangeVehicle(it.vehicle, it.previousVehicle) }
			on<PlayerLeftOrJoinVehicleEvent.Left> { onLeftVehicle(it.vehicle) }
		}
	}

	override fun onStop(): Job? {
		mainJob?.cancel()
		tickExecutorModule.remove(tickHandle)

		return super.onStop()
	}

	private fun onChangeVehicle(newVehicle: Vehicle, previousVehicle: Vehicle) {
		onLeftVehicle(previousVehicle)
		onJoinVehicle(newVehicle)
	}

	private fun preset(vehicle: Vehicle) {
		// Just got in the vehicle. Damage can not be multiplied this round
		// Set vehicle handling data
		fDeformationDamageMult = vehicle.handling.deformationDamageMultiplier
		fBrakeForce = vehicle.handling.brakeForce

		if (Cfg.weaponsDamageMultiplier != -1.0) {
			vehicle.handling.weaponDamageMultiplier =
				Cfg.weaponsDamageMultiplier / Cfg.damageFactorBody// Set weaponsDamageMultiplier && compensate for damageFactorBody
		}
		//Get the CollisionDamageMultiplier
		fCollisionDamageMult = vehicle.handling.collisionDamageMultiplier
		//Modify it by pulling all number a towards 1.0
		// Pull the handling file value closer to 1
		val newFCollisionDamageMultiplier = fCollisionDamageMult.pow(Cfg.collisionDamageExponent)
		vehicle.handling.collisionDamageMultiplier = newFCollisionDamageMultiplier

		//Get the EngineDamageMultiplier
		fEngineDamageMult = vehicle.handling.engineDamageMultiplier
		//Modify it by pulling all number a towards 1.0
		// Pull the handling file value closer to 1
		val newFEngineDamageMult = fEngineDamageMult.pow(Cfg.engineDamageExponent)
		vehicle.handling.engineDamageMultiplier = newFEngineDamageMult

		// If body damage catastrophic, reset somewhat so we can get new damage to multiply
		if (healthBodyCurrent < Cfg.cascadingFailureThreshold) {
			healthBodyNew = Cfg.cascadingFailureThreshold
		}
	}

	private fun onJoinVehicle(vehicle: Vehicle) {
		mainJob?.cancel()
		mainJob = launch {

			vehicleClass = vehicle.classType

			if (!isVehicleCanHandled(vehicleClass)) return@launch

			preset(vehicle)
			someFunc(vehicle)

			while (isActive) {
				delay(50)

				healthEngineCurrent = vehicle.engineHealth

				healthEngineNew = healthEngineCurrent
				healthEngineDelta = healthEngineLast - healthEngineCurrent
				healthEngineDeltaScaled = healthEngineDelta * Cfg.damageFactorEngine *
						Cfg.classDamageMultiplier[vehicleClass]

				healthBodyCurrent = vehicle.bodyHealth

				healthBodyNew = healthBodyCurrent
				healthBodyDelta = healthBodyLast - healthBodyCurrent
				healthBodyDeltaScaled = healthBodyDelta * Cfg.damageFactorBody *
						Cfg.classDamageMultiplier[vehicleClass]

				healthPetrolTankCurrent = vehicle.petrolTankHealth
				if (Cfg.compatibilityMode && healthPetrolTankCurrent < 1) {
					//	SetVehiclePetrolTankHealth(vehicle, healthPetrolTankLast)
					//	healthPetrolTankCurrent = healthPetrolTankLast
					healthPetrolTankLast = healthPetrolTankCurrent
				}

				healthPetrolTankNew = healthPetrolTankCurrent
				healthPetrolTankDelta = healthPetrolTankLast - healthPetrolTankCurrent
				healthPetrolTankDeltaScaled = healthPetrolTankDelta * Cfg.damageFactorPetrolTank *
						Cfg.classDamageMultiplier[vehicleClass]

				if (healthEngineCurrent > Cfg.engineSafeGuard + 1) {
					vehicle.setUndriveable(false)
				}

				if (healthEngineCurrent <= Cfg.engineSafeGuard + 1 && !Cfg.limpMode) {
					vehicle.setUndriveable(true)
				}

				// Damage happened while in the car = can be multiplied

				// Only do calculations if any damage is present on the car.
				// Prevents weird behavior when fixing using trainer or other script
				if (healthEngineCurrent != 1000.0 || healthBodyCurrent != 1000 || healthPetrolTankCurrent != 1000.0) {

					// Combine the delta values (Get the largest of the three)
					var healthEngineCombinedDelta = healthEngineDeltaScaled

					// If huge damage, scale back a bit
					if (healthEngineCombinedDelta > (healthEngineCurrent - Cfg.engineSafeGuard)) {
						healthEngineCombinedDelta *= 0.7
					}

					// If complete damage, but not catastrophic (ie. explosion territory) pull back a bit,
					// to give a couple of seconds og engine runtime before dying
					if (healthEngineCombinedDelta > healthEngineCurrent) {
						healthEngineCombinedDelta = healthEngineCurrent - (Cfg.cascadingFailureThreshold / 5)
					}


					//////- Calculate new value

					healthEngineNew = healthEngineLast - healthEngineCombinedDelta


					//////- Sanity Check on new values && further manipulations

					// If somewhat damaged, slowly degrade until slightly before cascading failure sets in, then stop

					if (healthEngineNew > (Cfg.cascadingFailureThreshold + 5) && healthEngineNew < Cfg.degradingFailureThreshold) {
						healthEngineNew -= (0.038 * Cfg.degradingHealthSpeedFactor)
					}

					// If Damage is near catastrophic, cascade the failure
					if (healthEngineNew < Cfg.cascadingFailureThreshold) {
						healthEngineNew -= (0.1 * Cfg.cascadingFailureSpeedFactor)
					}

					// Prevent Engine going to or below zero. Ensures you can reenter a damaged car.
					if (healthEngineNew < Cfg.engineSafeGuard) {
						healthEngineNew = Cfg.engineSafeGuard
					}

					// Prevent Explosions
					if (!Cfg.compatibilityMode && healthPetrolTankCurrent < 750) {
						healthPetrolTankNew = 750.0
					}

					// Prevent negative body damage.
					if (healthBodyNew < 0) {
						healthBodyNew = 0
					}
				}

				// set the actual new values
				if (healthEngineNew != healthEngineCurrent) {
					vehicle.engineHealth = healthEngineNew
				}
				if (healthBodyNew != healthBodyCurrent) {
					vehicle.bodyHealth = healthBodyNew
				}
				if (healthPetrolTankNew != healthPetrolTankCurrent) {
					vehicle.petrolTankHealth = healthPetrolTankNew
				}

				// Store current values, so we can calculate delta next time around
				healthEngineLast = healthEngineNew
				healthBodyLast = healthBodyNew
				healthPetrolTankLast = healthPetrolTankNew
				lastVehicle = vehicle
				if (Cfg.randomTireBurstInterval != 0 && vehicle.getSpeed() > 10) tireBurstLottery(
					vehicle
				)
			}
		}
	}

	private fun onLeftVehicle(lastVehicle: Vehicle) {
		tickExecutorModule.remove(tickHandle)
		mainJob?.cancel()

		lastVehicle.handling.brakeForce = fBrakeForce// Restore Brake Force multiplier

		if (Cfg.weaponsDamageMultiplier != -1.0) {
			lastVehicle.handling.weaponDamageMultiplier =
				Cfg.weaponsDamageMultiplier// Since we are out of the vehicle, we should no longer compensate for bodyDamageFactor
		}

		lastVehicle.handling.collisionDamageMultiplier =
			fCollisionDamageMult// Restore the original CollisionDamageMultiplier

		lastVehicle.handling.engineDamageMultiplier = fEngineDamageMult// Restore the original EngineDamageMultiplier
	}

	private fun someFunc(vehicle: Vehicle) {
		tickExecutorModule.remove(tickHandle)

		if (!Cfg.torqueMultiplierEnabled && !Cfg.preventVehicleFlip && !Cfg.limpMode) return

		tickExecutorModule.add(tickHandle) {
			if (Cfg.torqueMultiplierEnabled || Cfg.sundayDriver || Cfg.limpMode) {

				var factor = 1.0
				if (Cfg.torqueMultiplierEnabled && healthEngineNew < 900) {
					factor = (healthEngineNew + 200.0) / 1100
				}
				if (Cfg.sundayDriver && vehicle.classType != 14) { // Not for boats
					val accelerator = Client.getControlValue(2, 71)
					val brake = Client.getControlValue(2, 72)
					val speed = vehicle.getSpeedVector(true).y
					// Change Braking force
					var brk = fBrakeForce
					if (speed >= 1.0) {
						// Going forward
						if (accelerator > 127) {
							// Forward && accelerating
							val acc = fscale(
								accelerator.toDouble(),
								127.0,
								254.0,
								0.1,
								1.0,
								10.0 - (Cfg.sundayDriverAcceleratorCurve * 2.0)
							)
							factor *= acc
						}
						if (brake > 127) {
							// Forward && braking
							isBrakingForward = true
							brk = fscale(
								brake.toDouble(),
								127.0,
								254.0,
								0.01,
								fBrakeForce,
								10.0 - (Cfg.sundayDriverBrakeCurve * 2.0)
							)
						}
					} else if (speed <= -1.0) {
						// Going reverse
						if (brake > 127) {
							// Reversing && accelerating (using the brake)
							val rev = fscale(
								brake.toDouble(),
								127.0,
								254.0,
								0.1,
								1.0,
								10.0 - (Cfg.sundayDriverAcceleratorCurve * 2.0)
							)
							factor *= rev
						}
						if (accelerator > 127) {
							// Reversing && braking (Using the accelerator)
							isBrakingReverse = true
							brk = fscale(
								accelerator.toDouble(),
								127.0,
								254.0,
								0.01,
								fBrakeForce,
								10.0 - (Cfg.sundayDriverBrakeCurve * 2.0)
							)
						}
					} else {
						// Stopped or almost stopped or sliding sideways
						val entitySpeed = vehicle.getSpeed()
						if (entitySpeed < 1) {
							// Not sliding sideways
							if (isBrakingForward) {
								//Stopped or going slightly forward while braking
								NativeControls.Keys.VEH_BRAKE.disableControlAction() // Disable Brake until user lets go of brake
								vehicle.setForwardSpeed(speed * 0.98)
								vehicle.setBrakeLights(true)
							}
							if (isBrakingReverse) {
								//Stopped or going slightly in reverse while braking
								NativeControls.Keys.VEH_ACCELERATE.disableControlAction() // Disable reverse Brake until user lets go of reverse brake (Accelerator)
								vehicle.setForwardSpeed(speed * 0.98)
								vehicle.setBrakeLights(true)
							}
							if (isBrakingForward && NativeControls.Keys.VEH_BRAKE.getDisabledControlNormal() == 0) {
								// We let go of the brake
								isBrakingForward = false
							}
							if (isBrakingReverse && NativeControls.Keys.VEH_ACCELERATE.getDisabledControlNormal() == 0) {
								// We let go of the reverse brake (Accelerator)
								isBrakingReverse = false
							}
						}
					}
					if (brk > fBrakeForce - 0.02) {
						brk = fBrakeForce
					} // Make sure we can brake max.

					vehicle.handling.brakeForce = brk// Set new Brake Force multiplier
				}
				if (Cfg.limpMode && healthEngineNew < Cfg.engineSafeGuard + 5) {
					factor = Cfg.limpModeMultiplier
				}

				vehicle.setEngineTorqueMultiplier(factor)
			}
			if (Cfg.preventVehicleFlip) {
				val roll = vehicle.getRoll()
				if ((roll > 75.0 || roll < -75.0) && vehicle.getSpeed() < 2) {
					NativeControls.Keys.VEH_MOVE_LR.disableControlAction()// Disable left/right
					NativeControls.Keys.VEH_MOVE_UD.disableControlAction()// Disable up/down
				}
			}
		}
	}

	private fun isVehicleCanHandled(`class`: Int): Boolean {
		// We don"t want planes, helicopters, bicycles && trains
		if (`class` != 15 && `class` != 16 && `class` != 21 && `class` != 13) {
			return true
		}

		return false
	}

	private fun fscale(
		inputValue: Double,
		originalMin: Double,
		originalMax: Double,
		newBegin: Double,
		newEnd: Double,
		curve: Double
	): Double {
		var originalRange = 0.0
		var NewRange = 0.0
		var zeroRefCurVal = 0.0
		var normalizedCurVal = 0.0
		var rangedValue = 0.0
		var invFlag = 0

		var curve = curve
		var inputValue = inputValue

		if (curve > 10.0) curve = 10.0
		if (curve < -10.0) curve = -10.0

		curve = (curve * -.1)
		curve = 10.0.pow(curve)

		if (inputValue < originalMin)
			inputValue = originalMin

		if (inputValue > originalMax)
			inputValue = originalMax

		originalRange = originalMax - originalMin

		if (newEnd > newBegin) {
			NewRange = newEnd - newBegin
		} else {
			NewRange = newBegin - newEnd
			invFlag = 1
		}

		zeroRefCurVal = inputValue - originalMin
		normalizedCurVal = zeroRefCurVal / originalRange

		if (originalMin > originalMax) return 0.0


		rangedValue = if (invFlag == 0)
			normalizedCurVal.pow(curve) * NewRange + newBegin
		else
			newBegin - normalizedCurVal.pow(curve) * NewRange


		return rangedValue
	}


	private fun tireBurstLottery(vehicle: Vehicle) {
		val tireBurstNumber = Random.nextInt(tireBurstMaxNumber)
		if (tireBurstNumber == tireBurstLuckyNumber) {
			// We won the lottery, lets burst a tire.
			if (vehicle.tyresCanBurst) return
			val numWheels = vehicle.numberOfWheels
			var affectedTire: Int
			if (numWheels == 2)
				affectedTire = (Random.nextInt(2) - 1) * 4  // wheel 0 or 4
			else if (numWheels == 4) {
				affectedTire = (Random.nextInt(4) - 1)
				if (affectedTire > 1) affectedTire += 2     // 0, 1, 4, 5
			} else if (numWheels == 6)
				affectedTire = (Random.nextInt(6) - 1)
			else
				affectedTire = 0

			vehicle.wheels[affectedTire].burst(false, 1000.0)
			// Select a new number to hit, just in case some numbers occur more often than others
			tireBurstLuckyNumber = Random.nextInt(tireBurstMaxNumber)

		}
	}

	companion object {
		private object Cfg {

			// How much should the handling file deformation setting be compressed toward 1.0. (Make cars more similar). A value of 1=no change. Lower values will compress more, values above 1 it will expand. Dont set to zero or negative.
			const val collisionDamageExponent = 0.6

			// Sane values are 1 to 100. Higher values means more damage to vehicle. A good starting point is 10
			const val damageFactorEngine = 10.0

			// Sane values are 1 to 100. Higher values means more damage to vehicle. A good starting point is 10
			const val damageFactorBody = 10.0

			// Sane values are 1 to 200. Higher values means more damage to vehicle. A good starting point is 64
			const val damageFactorPetrolTank = 64.0

			// How much should the handling file engine damage setting be compressed toward 1.0. (Make cars more similar). A value of 1=no change. Lower values will compress more, values above 1 it will expand. Dont set to zero or negative.
			const val engineDamageExponent = 0.6

			// How much damage should the vehicle get from weapons fire. Range 0.0 to 10.0, where 0.0 is no damage and 10.0 is 10x damage. -1 = don't touch
			const val weaponsDamageMultiplier = 0.01

			// Speed of slowly degrading health, but not failure. Value of 10 means that it will take about 0.25 second per health point, so degradation from 800 to 305 will take about 2 minutes of clean driving. Higher values means faster degradation
			const val degradingHealthSpeedFactor = 10

			// Sane values are 1 to 100. When vehicle health drops below a certain point, cascading failure sets in, and the health drops rapidly until the vehicle dies. Higher values means faster failure. A good starting point is 8
			const val cascadingFailureSpeedFactor = 8.0

			// Below this value, slow health degradation will set in
			const val degradingFailureThreshold = 800.0

			// Below this value, health cascading failure will set in
			const val cascadingFailureThreshold = 360

			// Final failure value. Set it too high, and the vehicle won't smoke when disabled. Set too low, and the car will catch fire from a single bullet to the engine. At health 100 a typical car can take 3-4 bullets to the engine before catching fire.
			const val engineSafeGuard = 100.0

			// Decrease engine torque as engine gets more and more damaged
			const val torqueMultiplierEnabled = true

			// If true, the engine never fails completely, so you will always be able to get to a mechanic unless you flip your vehicle and preventVehicleFlip is set to true
			const val limpMode = false

			// The torque multiplier to use when vehicle is limping. Sane values are 0.05 to 0.25
			const val limpModeMultiplier = 0.15

			// If true, you can't turn over an upside down vehicle
			const val preventVehicleFlip = true

			// If true, the accelerator response is scaled to enable easy slow driving. Will not prevent full throttle. Does not work with binary accelerators like a keyboard. Set to false to disable. The included stop-without-reversing and brake-light-hold feature does also work for keyboards.
			const val sundayDriver = true

			// The response curve to apply to the accelerator. Range 0.0 to 10.0. Higher values enables easier slow driving, meaning more pressure on the throttle is required to accelerate forward. Does nothing for keyboard drivers
			const val sundayDriverAcceleratorCurve = 7.5

			// The response curve to apply to the Brake. Range 0.0 to 10.0. Higher values enables easier braking, meaning more pressure on the throttle is required to brake hard. Does nothing for keyboard drivers
			const val sundayDriverBrakeCurve = 5.0

			// prevents other scripts from modifying the fuel tank health to avoid random engine failure with BVA 2.01 (Downside is it disabled explosion prevention)
			const val compatibilityMode = false

			// Number of minutes (statistically, not precisely) to drive above 22 mph before you get a tire puncture. 0=feature is disabled
			const val randomTireBurstInterval = 120


			// Class Damagefactor Multiplier
			// The damageFactor for engine, body and Petroltank will be multiplied by this value, depending on vehicle class
			// Use it to increase or decrease damage for each class

			val classDamageMultiplier = mutableListOf(
				1.0,        //	0: Compacts
				1.0,        //	1: Sedans
				1.0,        //	2: SUVs
				1.0,        //	3: Coupes
				1.0,        //	4: Muscle
				1.0,        //	5: Sports Classics
				1.0,        //	6: Sports
				1.0,        //	7: Super
				0.25,       //	8: Motorcycles
				0.7,        //	9: Off-road
				0.25,       //	10: Industrial
				1.0,        //	11: Utility
				1.0,        //	12: Vans
				1.0,        //	13: Cycles
				0.5,        //	14: Boats
				1.0,        //	15: Helicopters
				1.0,        //	16: Planes
				1.0,        //	17: Service
				0.75,       //	18: Emergency
				0.75,       //	19: Military
				1.0,        //	20: Commercial
				1.0         //	21: Trains
			)
		}
	}
}