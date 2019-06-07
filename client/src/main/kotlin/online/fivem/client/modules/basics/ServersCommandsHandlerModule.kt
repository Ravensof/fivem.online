package online.fivem.client.modules.basics

import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.events.PauseMenuStateChangedEvent
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.*
import kotlin.js.Date

class ServersCommandsHandlerModule(
	private val spawnManagerModule: SpawnManagerModule,
	private val joinTransitionModule: JoinTransitionModule
) : AbstractClientModule() {

	private var lastSync = 0.0

	private val syncData = ClientSideSynchronizationEvent()

	init {
		Event.apply {
			on<PauseMenuStateChangedEvent.Enabled> {
				if (Date.now() - lastSync >= SYNC_TIME_THRESHOLD_MILLISECONDS) synchronizeToServer()
			}
//			on<SpawnVehicleEvent> { onVehicleSpawn(it) }
		}
	}

	override fun onStart() = launch {
		spawnManagerModule.waitForStart()
		joinTransitionModule.waitForStart()

		ServerEvent.apply {
			on<StopResourceEvent> { onStopEvent(it.eventId) }
			on<ServerSideSynchronizationEvent> { onServerRequest(it) }
			on<SpawnPlayerEvent> { onPlayerSpawn(it.coordinatesX, it.model) }
		}
	}

//	private fun onVehicleSpawn(event: SpawnVehicleEvent) {
//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//		}
//	}

	private fun onStopEvent(eventId: Int) = launch {
		synchronizeToServer().join()
		ServerEvent.emit(AcceptEvent(eventId))
		moduleLoader.stop()
	}

	private fun onPlayerSpawn(coordinatesX: CoordinatesX, model: Int?) = launch {
		joinTransitionModule.startTransition(this@ServersCommandsHandlerModule)
		spawnManagerModule.spawnPlayerJob(coordinatesX, model).join()
		joinTransitionModule.endTransition(this@ServersCommandsHandlerModule)
	}

	private suspend fun onServerRequest(event: ServerSideSynchronizationEvent) {

		launch {
			moduleLoader.getLoadedModules().forEach {
				if (it !is AbstractClientModule) return@forEach

				launch {
					it.onSync(event)?.join()
				}
			}
		}.join()

		synchronizeToServer()
	}

	private fun synchronizeToServer() = launch {

		moduleLoader.getLoadedModules().forEach { module ->
			if (module !is AbstractClientModule) return@forEach

			module.onSync(syncData)?.join()
		}

		ServerEvent.emit(syncData)
		lastSync = Date.now()
	}

	companion object {
		const val SYNC_TIME_THRESHOLD_MILLISECONDS = 5_000
	}
}