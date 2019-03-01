package online.fivem.client.modules.vehicle.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import online.fivem.client.entities.Vehicle
import online.fivem.common.common.createJob
import online.fivem.common.extensions.repeatJob
import kotlin.coroutines.CoroutineContext

/*
fInitialDriveForce float
fInitialDriveMaxFlatVel float

fInitialDriveMaxFlatVel would be set to 220
FINITIALDRAGCOEFF would be set to 8.0
and FINITIALDRIVEFORCE would be set to 0.35

крутящий момент
opposite force

обороты * крутящий момент / сила противодействия

сила противодействия = передаточное число * сила сопротивления
 */

class Engine(
	private val vehicle: Vehicle,

	private val transmission: Transmission

) : CoroutineScope {

	override val coroutineContext: CoroutineContext = createJob()
	private var engine: Job? = null

	val maxFuelConsumption = 10.0 // литров в час
	val minFuelConsumption = 1.0 / maxFuelConsumption

	var currentRPM = 0.0

	fun start() {
		if (engine != null) return

		engine = repeatJob(UPDATE_INTERVAL) {
			vehicle.fuelLevel -= currentRPM * minFuelConsumption * UPDATE_INTERVAL
		}
	}

	fun stop() = launch {
		engine?.cancelAndJoin()
		engine = null
	}

	fun getForce() {

	}

	private fun convertFuelConsumption(litresPer100km: Double) {

	}

	companion object {
		const val MEASURE_RPM_ON_TICK = 1_000
		const val UPDATE_INTERVAL = 1_000L
	}
}