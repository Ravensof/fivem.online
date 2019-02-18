package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.events.PlayerSpawnedEvent
import online.fivem.client.extensions.networkResurrectLocalPlayer
import online.fivem.client.extensions.requestCollisionAtCoordinates
import online.fivem.client.extensions.setPlayerModelSync
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.SEvent
import online.fivem.common.common.Stack
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.SpawnVehicleEvent
import kotlin.coroutines.CoroutineContext

class SpawnManagerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val api by moduleLoader.onReady<API>()

	private fun vehicleSpawn(event: SpawnVehicleEvent) {

//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//			Client.setVehicleWheelHealth(vehicle)
//		}
	}

	fun spawnPlayerJob(coordinatesX: CoordinatesX, modelHash: Int?): Job = launch {

		val fadeHandle = api.doScreenFadeOutAsync(500).await()
		api.setPlayerCoordinates(CoordinatesX(0f, 0f, 0f, 0f))
		val playerId = Client.getPlayerId()

		freezePlayer(playerId, true)

		modelHash?.let {
			Client.setPlayerModelSync(playerId, it)
		}

		val ped = Client.getPlayerPedId()

		Client.networkResurrectLocalPlayer(coordinatesX)
		Client.clearPedTasksImmediately(ped)
		Client.removeAllPedWeapons(ped)
		Client.clearPlayerWantedLevel(playerId)

		Client.requestCollisionAtCoordinates(coordinatesX)//todo не работает?
		while (!Client.hasCollisionLoadedAroundEntity(ped)) {
			delay(1000)
		}
		api.setPlayerCoordinates(coordinatesX)

		Client.shutdownLoadingScreen()

		api.doScreenFadeInJob(fadeHandle, 500).join()
		freezePlayer(playerId, false)

		SEvent.emit(PlayerSpawnedEvent())
	}

	private var lockControlHandle = Stack.UNDEFINED_INDEX

	private fun freezePlayer(playerSrc: Int, freeze: Boolean) {

		val ped = Client.getPlayerPedId()

		api.unLockControl(lockControlHandle)
		if (!freeze) {

			if (!Client.isEntityVisible(ped)) {
				Client.setEntityVisible(ped, true)
			}

			if (!Client.isPedInAnyVehicle(ped)) {
				Client.setEntityCollision(ped, true)
			}

			Client.setEntityKinematic(ped, false)
			Client.setPlayerInvincible(playerSrc, false)

		} else {
			lockControlHandle = api.lockControl()
			if (Client.isEntityVisible(ped)) {
				Client.setEntityVisible(ped, false)
			}

			Client.setEntityCollision(ped, false)
			Client.setEntityKinematic(ped, true)
			Client.setPlayerInvincible(playerSrc, true)

			if (Client.isPedFatallyInjured(ped)) {
				Client.clearPedTasksImmediately(ped)
			}
		}
	}
}

