package online.fivem.client.modules.basics

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.common.events.net.*
import online.fivem.common.extensions.forEach
import kotlin.js.Date

class ServersCommandsHandlerModule(
	private val spawnManagerModule: SpawnManagerModule,
	private val joinTransitionModule: JoinTransitionModule,
	private val stateRepositoryModule: StateRepositoryModule
) : AbstractClientModule() {

	private var lastSync = 0.0

	override fun onStartAsync() = async {
		spawnManagerModule.waitForStart()
		joinTransitionModule.waitForStart()
		stateRepositoryModule.waitForStart()

		subscribeOnPauseMenuState()

		ServerEvent.apply {
			on<StopResourceEvent> { onStopEvent(it.eventId) }
			on<ServerSideSynchronizationEvent> { onServerRequest(it) }
			on<SpawnPlayerEvent> { onPlayerSpawn(it) }
		}
	}

//	private fun onVehicleSpawn(event: SpawnVehicleEvent) {
//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//		}
//	}

	private fun subscribeOnPauseMenuState() = launch {
		stateRepositoryModule.isPauseMenuEnabled.openSubscription().forEach { isPause ->
			if (isPause && Date.now() - lastSync >= SYNC_TIME_THRESHOLD_MILLISECONDS) {
				synchronizeToServer()
			}
		}
	}

	private fun onStopEvent(eventId: Int) = launch {
		synchronizeToServer().join()
		ServerEvent.emit(AcceptEvent(eventId))
		moduleLoader.stop()
	}

	private fun onPlayerSpawn(event: SpawnPlayerEvent) = launch {
		joinTransitionModule.startTransition(this@ServersCommandsHandlerModule)
		spawnManagerModule.spawnPlayerPed(event).join()
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

		val syncData = ClientSideSynchronizationEvent()

		moduleLoader.getLoadedModules().forEach { module ->
			if (module !is AbstractClientModule) return@forEach

			module.onSaveState(syncData)?.join()
		}

		ServerEvent.emit(syncData)
		lastSync = Date.now()
	}

	companion object {
		const val SYNC_TIME_THRESHOLD_MILLISECONDS = 5_000
	}
}