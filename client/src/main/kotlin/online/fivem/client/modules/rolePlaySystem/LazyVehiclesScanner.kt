package online.fivem.client.modules.rolePlaySystem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.common.VehiclesIterator
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Entity
import kotlin.coroutines.CoroutineContext

class LazyVehiclesScanner(
	override val coroutineContext: CoroutineContext,
	private val tickExecutorModule: TickExecutorModule
) : CoroutineScope {

	fun getPlayersVehicles(): Deferred<MutableList<Pair<Entity, Int>>?> = async {

		val scannerChannel = Channel<Pair<Entity, Int>>(GlobalConfig.MAX_PLAYERS)
		val vehiclesIterator = VehiclesIterator()

		val executorId = tickExecutorModule.add { scanNext(vehiclesIterator, scannerChannel) }

		val list = mutableListOf<Pair<Entity, Int>>()

		for (data in scannerChannel) {
			list.add(data.first to data.second)
		}

		tickExecutorModule.remove(executorId)
		vehiclesIterator.close()

		return@async list
	}

	private fun scanNext(vehiclesIterator: VehiclesIterator, scannerChannel: Channel<Pair<Entity, Int>>) {
		if (!vehiclesIterator.hasNext()) scannerChannel.close()

		val entity = vehiclesIterator.next()
		val id = Client.getVehicleOilLevel(entity)?.takeIf { it < 0 }?.toInt() ?: return

		launch {
			scannerChannel.send(entity to id)
		}
	}
}