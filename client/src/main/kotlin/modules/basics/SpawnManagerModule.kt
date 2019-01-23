package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.extensions.networkResurrectLocalPlayer
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.PlayerSpawnedEvent
import online.fivem.common.events.net.SpawnVehicleEvent
import kotlin.coroutines.CoroutineContext

class SpawnManagerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	override fun start(): Job? {
		return super.start()
	}

	private fun vehicleSpawn(event: SpawnVehicleEvent) {
		launch {
			//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//			Client.setVehicleWheelHealth(vehicle)
		}
	}

	fun spawnPlayer(coordinatesX: CoordinatesX, modelHash: Int?): Job = launch {

		Client.doScreenFadeOut(500).join()
		Client.setEntityCoordsNoOffset(Client.getPlayerPed(), 0, 0, 0, zAxis = true)
		val playerId = Client.getPlayerId()

		freezePlayer(playerId, true)

		modelHash?.let {
			Client.requestModel(it).join()

			Client.setPlayerModel(playerId, it)
			Client.setModelAsNoLongerNeeded(it)
		}

//			Client.requestCollisionAtCoordinates(coordinatesX)//todo не работает?

		val ped = Client.getPlayerPed()

		Client.setEntityCoordsNoOffset(ped, coordinatesX.x, coordinatesX.y, coordinatesX.z, zAxis = true)
		Client.networkResurrectLocalPlayer(coordinatesX)
		Client.clearPedTasksImmediately(ped)
		Client.removeAllPedWeapons(ped)
		Client.clearPlayerWantedLevel(playerId)

//			while (!Client.hasCollisionLoadedAroundEntity(ped)) {
//				delay(1000)
//			}

		Client.shutdownLoadingScreen()

		Client.doScreenFadeIn(500).join()

		freezePlayer(playerId, false)

		UEvent.emit(PlayerSpawnedEvent())
	}

	private fun freezePlayer(playerSrc: Int, freeze: Boolean) {
		Client.setPlayerControl(playerSrc, !freeze, 0)

		val ped = Client.getPlayerPed()

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

