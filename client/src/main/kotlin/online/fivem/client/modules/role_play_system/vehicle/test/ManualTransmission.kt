package online.fivem.client.modules.role_play_system.vehicle.test

import kotlinx.coroutines.*
import online.fivem.Natives
import online.fivem.client.entities.Vehicle
import online.fivem.client.extensions.disableControlAction
import online.fivem.client.extensions.drawScreenText2D
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.common.Console
import online.fivem.common.common.Utils.normalizeToLimits
import online.fivem.common.extensions.*
import online.fivem.common.gtav.NativeControls
import kotlin.coroutines.CoroutineContext
import kotlin.math.pow

//https://forum.fivem.net/t/discussion-research-manual-transmission-resource/146444/7
//TODO доработать
class ManualTransmission(
	override val coroutineContext: CoroutineContext,

	private val vehicle: Vehicle,
	private val tickExecutorModule: TickExecutorModule
) : CoroutineScope {
	val gearsRatio: List<Double> = listOf(
		-3.223,

		3.166,
		1.882,
		1.296,
		0.972,
		0.738
	)

	var currentGear = 0
		set(value) {
			field = normalizeToLimits(value, 0, gearsRatio.size - 1)
		}

	fun gearsCount() = gearsRatio.size

	fun getGearRatio(gear: Int): Double {
		return gearsRatio.getOrNull(gear).orZero()
	}

	fun getMaxSpeed(gear: Int): Double {
		return SPEED_PER_GEAR * (1.0 + getGearRatio(gear))
	}

	private var engine: Job? = null

	fun start() {
		engine = launch {

			while (isActive) {
				//Get information about the new vehicle

				Console.debug("Got new vehicle info")
				resetLastVehicle()

				vstTheoreticalMaxSpeed = Natives.getVehicleHandlingFloat(
					vehicle.entity,
					"CHandlingData",
					"fInitialDriveMaxFlatVel"
				) * 1.32
				vstAcceleration =
					Natives.getVehicleHandlingFloat(vehicle.entity, "CHandlingData", "fInitialDriveForce")
				vstNumberOfGears = vehicle.highGear

				vehicle.highGear = 1

				simulateGears()


				rtEngineRpm = vehicle.currentRpm
				rtVehicleSpeed = vehicle.dashboardSpeed * 3.6

				NativeControls.Keys.VEH_CIN_CAM.disableControlAction()
				NativeControls.Keys.SPRINT.disableControlAction()
				//DisableControlAction(2, 72, true)

				//Shift up and down
				if (Natives.isDisabledControlJustPressed(0, 21)) {
					currentGear += 1
					simulateGears()
				} else if (Natives.isDisabledControlJustPressed(0, 80)) {
					currentGear -= 1
					simulateGears()
				}

				simulateClutch()
				simulateEngine()
				simulateSpeed()


				delay(1)
			}
		}
	}

	fun stop() {
		engine?.cancel()
	}

	val acclerationInputValue = -1
	var engineRpm = 0.0
	val simulatedEngineRpm = 0

	//-------------------------------------
// Real time variables (rt -> realtime)
	var rtEngineRpm = 0.0
	var rtVehicleSpeed = 0.0

	//-------------------------------------
// Vehicle stats variables (vst -> vehicle stat)
	var vstTheoreticalMaxSpeed = 0.0
	var vstAcceleration = 0.0
	var vstNumberOfGears = 0

	//-------------------------------------
// Gear variables (g -> gear)
	var gMaxGears = 0
	var gGearDiff = 0.0
	var gGearSpeedMin = 0.0
	var gGearSpeedMax = 0.0
	var gGearSpeedPrime = 0.0
	val gConstStallingFactor = 0.2// - 10 % of gGearSpeedMin is the stalling range .
	val gConstOverevvingFactor = 0.2// + 10 % of gGearSpeedMax is the over revving range.

	var gGearCurrentRatio = 0.0

	//-------------------------------------
// Engine variables (e -> engine)
	var eEngineRpm = 0.0
	private val eConstEngineIdleRpm = 0.2

	//-------------------------------------
// Throttle variables (t -> throttle)
	var tThrottleRaw = 0
	var tThrottleFull = 0.0

	//TODO: Only run when the engine is started
	//TODO: Don't simulate on basis of km/h, use m/s instead.
	init {
		launch {
			while (isActive) {

				listOf(
					"-= Realtime",
					"rtVehicleSpeed: $rtVehicleSpeed",
					"rtEngineRpm: $rtEngineRpm",

					"-== Vehicle stats",
					"vstTheoreticalMaxSpeed: $vstTheoreticalMaxSpeed",
					"vstAcceleration: $vstAcceleration",
					"vstNumberOfGears: $vstNumberOfGears",

					"-== Gearbox",
					"gGearCurrent: $currentGear",
					"gGearDiff: $gGearDiff",
					"gGearCurrentRatio: $gGearCurrentRatio",
					"gGearSpeedMin: $gGearSpeedMin",
					"gGearSpeedMax: $gGearSpeedMax",
					"gGearSpeedPrime: $gGearSpeedPrime",

					"-== Engine",
					"eEngineRpm: $eEngineRpm",
					"eConstEngineIdleRpm: $eConstEngineIdleRpm",

					"-== Throttle",
					"tThrottleRaw: $tThrottleRaw",
					"tThrottleFull: $tThrottleFull"
				).forEachIndexed { index, string ->
					Natives.drawScreenText2D(0.01, 0.01 + index * 2, string)
				}

				delay(1)
			}
		}
	}

	private fun simulateGears() {
		currentGear = normalizeToLimits(currentGear, -1, vstNumberOfGears)

		//TODO: What do we need to do to make reverse happen?
		//TODO: What do we need to do when in neutral?

		// We are in neutral or reverse, we don't have anything to simulate...
		//      Clear out the gear variables just in case.
		if (currentGear == 0) {
			gGearSpeedMin = 0.0
			gGearSpeedMax = 0.0
			gGearSpeedPrime = 0.0
			gGearCurrentRatio = 1.0
			return
		} else if (currentGear == 1) {
			gGearSpeedMin = 0.0
			gGearSpeedMax = 60.0
		}

		val gearCurrentClamped = normalizeToLimits(currentGear, 1, vstNumberOfGears)
		gGearCurrentRatio = 1.0 + getGearRatio(gearCurrentClamped) / 10
		val speedPerGear = (vstTheoreticalMaxSpeed / vstNumberOfGears) * gGearCurrentRatio

		// Speed min is the top speed of the previous gear excluding over-revving %.
		//      Don't include reverse and neutral
		// TODO: Previous gear needs to take in account the gear ratio of the previous gear.
		val previousGear = normalizeToLimits(currentGear - 1, 0, vstNumberOfGears)
		val speedPreviousGear = previousGear * speedPerGear
		gGearSpeedMin = speedPreviousGear * (1.0 - gConstStallingFactor) // Minimum speed for this gear
		gGearSpeedMax = (currentGear * speedPerGear) * (1.0 + gConstOverevvingFactor) // Max speed for this gear
		gGearSpeedPrime = percentageToValueInRange(
			speedPreviousGear,
			gGearSpeedMax,
			0.7
		).toDouble() // The best speed to shift (i.e. b)
		gGearDiff = normalizeToLimits((currentGear.toDouble() / vstNumberOfGears), 0.25, 10.0) *
				(1.0 - vstAcceleration) // The difference in gears, "1.0 - vstAcceleration" is for cars with low engine power
	}

	private fun simulateClutch() {
		// Tell the game we are in neutral, we don't want to spin the tires in neutral.
		if (currentGear == 0) {
			Natives.setVehicleCurrentGear(vehicle.entity, 0)
			Natives.setVehicleNextGear(vehicle.entity, 0)
		}
	}

	private fun simulateEngine() {
		// TODO: Stall if < 0.2 rpm
		// TODO: Overrevving

		tThrottleRaw = Natives.getControlNormal(0, 71)
		tThrottleFull = (vstAcceleration * tThrottleRaw * Natives.getFrameTime() * 100) * gGearDiff

		when (currentGear) {
			0 ->
				if (tThrottleRaw > 0) {
					Natives.setVehicleCurrentRpm(vehicle.entity, -1.0)
				} else {
					Natives.setVehicleCurrentRpm(vehicle.entity, 0.0)
				}

			1 ->
				if (tThrottleRaw > 0)
					vehicle.currentRpm = 1.0
				else
					vehicle.currentRpm = 0.0
			else -> {
				// Returns the RPM, unclamped.
				eEngineRpm = valueToPercentageInRange(gGearSpeedMin, gGearSpeedMax, rtVehicleSpeed).toDouble() +
						eConstEngineIdleRpm + tThrottleFull
				engineRpm = exponentialCurve(engineRpm, gGearDiff / 100)
				val rpm = normalizeToLimits(eEngineRpm, 0.0, 1.0)
				Natives.setVehicleCurrentRpm(vehicle.entity, rpm)
			}
		}
	}

	private fun simulateSpeed() {
		//Note: don't lock speed in max gear.
		val gearMaxSpeedInMs = gGearSpeedMax * 0.277778
		vehicle.maxSpeed = gearMaxSpeedInMs
	}

// TODO: Reduce RPM to a nominal value when shifting up.
// TODO: Multiply RPM by some curve (sigmoid?)
// TODO: Enable up/down shifting with proper RPM
// TODO: Neutral and reverse... how would this work?
// TODO: Overlapping RPM/speed ranges, allowing for stalling and overrevving
// TODO: Allow revving in neutral
// TODO: Disallow reversing when not in neutral... or instanly explode the car :thinking:

	// TODO: A more solid way of resetting a car.
	private fun resetLastVehicle() {
		//TODO: This needs cleaning up.

		engineRpm = 0.0
		currentGear = 0
		gGearSpeedMin = 0.0
		gGearSpeedMax = 0.0
		gGearSpeedPrime = 0.0
		gMaxGears = 0
		gGearCurrentRatio = 0.0
		vstNumberOfGears = 0
		vstAcceleration = 0.0
		vstTheoreticalMaxSpeed = 0.0
		rtEngineRpm = 0.0
		rtVehicleSpeed = 0.0
		eEngineRpm = 0.0
		tThrottleRaw = 0
		tThrottleFull = 0.0
		Console.debug("Vehicle info has been reset")
	}

	private fun valueToPercentageInRange(min: Number, max: Number, input: Number): Number {
		return (input - min) / (max - min)
	}

	private fun percentageToValueInRange(min: Number, max: Number, input: Number): Number {
		return input * (max - min) + min
	}

	private fun exponentialCurve(n: Double, k: Double): Double {
		return n.pow(k) - 1 / n - 1
	}

	companion object {
		const val SPEED_PER_GEAR = 45
	}
}