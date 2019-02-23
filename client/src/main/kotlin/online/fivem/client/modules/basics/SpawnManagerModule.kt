package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.common.Player
import online.fivem.client.events.PlayerSpawnedEvent
import online.fivem.client.extensions.networkResurrectLocalPlayer
import online.fivem.client.extensions.requestCollisionAtCoordinates
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Event
import online.fivem.common.common.Stack
import online.fivem.common.entities.CoordinatesX
import kotlin.coroutines.CoroutineContext

class SpawnManagerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val api by moduleLoader.onReady<API>()

//	private fun vehicleSpawn(event: SpawnVehicleEvent) {
//
//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//			Client.setVehicleWheelHealth(vehicle)
//		}
//	}

	fun spawnPlayerJob(coordinatesX: CoordinatesX, modelHash: Int?): Job = launch {

		val fadeHandle = api.doScreenFadeOutAsync(500).await()
		player.ped.coordinates = CoordinatesX(0f, 0f, 0f, 0f)

		freezePlayer(player, true)

		modelHash?.let {
			player.setModel(it)
		}

		val ped = player.ped

		Client.networkResurrectLocalPlayer(coordinatesX)
		ped.clearTasksImmediately()
		ped.removeAllWeapons()
		player.clearWantedLevel()

		Client.requestCollisionAtCoordinates(coordinatesX)
		while (!Client.hasCollisionLoadedAroundEntity(ped.entity)) {
			delay(1000)
		}
		player.ped.coordinates = coordinatesX

		Client.shutdownLoadingScreen()

		api.doScreenFadeInJob(fadeHandle, 500).join()
		freezePlayer(player, false)

		Event.emit(PlayerSpawnedEvent())
	}

	private var lockControlHandle = Stack.UNDEFINED_INDEX

	private fun freezePlayer(player: Player, freeze: Boolean) {

		val ped = player.ped

		api.unLockControl(lockControlHandle)
		if (!freeze) {

			ped.isVisible = true
			ped.isInAVehicle()

			if (!ped.isInAVehicle()) {
				ped.setCollision(true)
			}

			ped.freezePosition(false)
			player.isInvincible = false

		} else {
			lockControlHandle = api.lockControl()

			ped.isVisible = false
			ped.setCollision(false)
			ped.freezePosition(true)
			player.isInvincible = true

			if (Client.isPedFatallyInjured(ped.entity)) {
				ped.clearTasksImmediately()
			}
		}
	}
}

