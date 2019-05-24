package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.events.PauseMenuStateChangedEvent
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.common.Serializer
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.common.events.net.StopResourceEvent
import online.fivem.common.events.net.SyncEvent
import online.fivem.common.extensions.serializeToPacket
import online.fivem.common.other.KotlinXSerializationPacket
import kotlin.js.Date

class ServersCommandsHandlerModule : AbstractClientModule() {

	private lateinit var dateTime: DateTimeModule
	private lateinit var weatherModule: WeatherModule

	private lateinit var spawnManager: SpawnManagerModule
	private lateinit var joinTransition: JoinTransitionModule

	private var lastSync = 0.0

	override fun onInit() {
		Event.apply {
			on<PauseMenuStateChangedEvent.Enabled> {
				if (Date.now() - lastSync >= SYNC_TIME_THRESHOLD_MILLISECONDS) synchronizeToServer()
			}
//			on<SpawnVehicleEvent> { onVehicleSpawn(it) }
		}
	}

	override fun onStart() = launch {
		dateTime = moduleLoader.getModule(DateTimeModule::class)
		weatherModule = moduleLoader.getModule(WeatherModule::class)
		spawnManager = moduleLoader.getModule(SpawnManagerModule::class)
		joinTransition = moduleLoader.getModule(JoinTransitionModule::class)

		ServerEvent.apply {
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

	override fun onStop(): Job? {
		return launch {
			synchronizeToServer()
			NuiEvent.emit(StopResourceEvent())
		}
	}

	private fun onPlayerSpawn(coordinatesX: CoordinatesX, model: Int?) = launch {
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

		val syncData = mutableListOf<KotlinXSerializationPacket>()

		moduleLoader.getModules().forEach { module ->
			if (module !is AbstractClientModule) return@forEach

			module.onSave()?.let { data ->
				syncData.add(
					Serializer.serializeToPacket(data)
				)
			}
		}

		ServerEvent.emit(SyncEvent(syncData))

		lastSync = Date.now()
	}

	companion object {
		const val SYNC_TIME_THRESHOLD_MILLISECONDS = 5_000
	}
}