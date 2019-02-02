package online.fivem.client.modules.rolePlaySystem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.JoinTransitionModule
import online.fivem.client.modules.basics.SpawnManagerModule
import online.fivem.client.modules.serverEventExchanger.ServerEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.RequestPackEvent
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.common.events.net.SynchronizeEvent
import kotlin.coroutines.CoroutineContext

class SynchronizationModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val spawnManager by moduleLoader.onReady<SpawnManagerModule>()
	private val joinTransition by moduleLoader.onReady<JoinTransitionModule>()

	override fun onInit() {
		ServerEvent.on<RequestPackEvent> { onServerRequest(it.kClasses) }
		ServerEvent.on<SpawnPlayerEvent> { onPlayerSpawn(it.coordinatesX, it.model) }
//		ServerEvent.on<SpawnVehicleEvent> { onVehicleSpawn(it) }
	}

//	private fun onVehicleSpawn(event: SpawnVehicleEvent) {
//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//		}
//	}

	private fun onPlayerSpawn(coordinatesX: CoordinatesX, model: Int) {
		launch {
			joinTransition.startTransition().join()
			spawnManager.spawnPlayer(coordinatesX, model).join()
			joinTransition.endTransition()
		}
	}

	private fun onServerRequest(kClasses: List<String>) {

		val playerPed = Client.getPlayerPed()

		ServerEvent.emit(
			SynchronizeEvent(
				coordinatesX = Client.getEntityCoords(playerPed)?.let {
					CoordinatesX(
						it,
						Client.getEntityHeading(playerPed)
					)
				}

			)
		)
	}
}