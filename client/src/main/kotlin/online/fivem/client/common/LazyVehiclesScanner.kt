package online.fivem.client.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.entities.Vehicle
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.GlobalConfig
import online.fivem.common.extensions.forEach
import kotlin.coroutines.CoroutineContext

class LazyVehiclesScanner(
	override val coroutineContext: CoroutineContext,
	private val tickExecutorModule: TickExecutorModule
) : CoroutineScope {

	fun findVehiclesAsync(predicate: (Vehicle) -> Boolean): Deferred<List<Vehicle>> = async {

		val scannerChannel = Channel<Vehicle>(GlobalConfig.MAX_PLAYERS)
		val vehiclesIterator = VehiclesIterator()

		tickExecutorModule.add(this@LazyVehiclesScanner) { scanNext(vehiclesIterator, scannerChannel, predicate) }

		val list = mutableListOf<Vehicle>()

		scannerChannel.forEach { list.add(it) }

		tickExecutorModule.remove(this@LazyVehiclesScanner)
		vehiclesIterator.close()

		return@async list
	}

	private fun scanNext(
		vehiclesIterator: VehiclesIterator,
		scannerChannel: Channel<Vehicle>,
		predicate: (Vehicle) -> Boolean
	) {
		if (!vehiclesIterator.hasNext()) scannerChannel.close()

		val entity = vehiclesIterator.next()
		val vehicle = Vehicle.newInstance(entity)

		if (!predicate(vehicle)) return

		launch {
			scannerChannel.send(vehicle)
		}
	}
}