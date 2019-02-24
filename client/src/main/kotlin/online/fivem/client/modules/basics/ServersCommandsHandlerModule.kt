package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.events.PauseMenuStateChangedEvent
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.ClientSideSynchronizeEvent
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.common.events.net.StopResourceEvent
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class ServersCommandsHandlerModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	private val dateTime by moduleLoader.onReady<DateTimeModule>()
	private val weatherModule by moduleLoader.onReady<WeatherModule>()

	private val spawnManager by moduleLoader.onReady<SpawnManagerModule>()
	private val joinTransition by moduleLoader.onReady<JoinTransitionModule>()

	private var lastSync = 0.0

	override fun onInit() {
		ServerEvent.apply {
			on<ServerSideSynchronizationEvent> { onServerRequest(it) }
			on<SpawnPlayerEvent> { onPlayerSpawn(it.coordinatesX, it.model) }
		}

		Event.apply {
			on<PauseMenuStateChangedEvent.Enabled> {
				if (Date.now() - lastSync >= SYNC_TIME_THRESHOLD_MILLISECONDS) synchronizeToServer()
			}
//			on<SpawnVehicleEvent> { onVehicleSpawn(it) }
		}
	}

//	private fun onVehicleSpawn(event: SpawnVehicleEvent) {
//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//		}
//	}

	override fun onStop(): Job? {
		return launch {
			synchronizeToServer()
			NuiEvent.emit(StopResourceEvent())
		}
	}

	private fun onPlayerSpawn(coordinatesX: CoordinatesX, model: Int) = launch {
		joinTransition.startTransitionJob().join()
		spawnManager.spawnPlayerJob(coordinatesX, model).join()
		joinTransition.endTransitionJob()
	}

	private suspend fun onServerRequest(event: ServerSideSynchronizationEvent) {

		dateTime.date.serverRealTime = event.serverTime

		event.weather?.let {
			weatherModule.weatherQueue.send(it)
		}

		synchronizeToServer()
	}

	private suspend fun synchronizeToServer() {
		val playerPed = Client.getPlayerPedId()

		ServerEvent.emit(
			ClientSideSynchronizeEvent(
				coordinatesX = CoordinatesX(
					Client.getEntityCoords(playerPed),
					Client.getEntityHeading(playerPed)
				)
			)
		)

		lastSync = Date.now()
	}

	companion object {
		const val SYNC_TIME_THRESHOLD_MILLISECONDS = 5_000
	}
}