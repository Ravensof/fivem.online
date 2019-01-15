package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import online.fivem.client.extensions.networkResurrectLocalPlayer
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.UEvent
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.PlayerSpawnedEvent
import kotlin.coroutines.CoroutineContext

class SpawnManagerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	override fun start(): Job? {
		return super.start()
	}

	fun spawnPlayer(coordinatesX: CoordinatesX, modelHash: Int?): Job = launch {
		try {
			Console.debug("Client.doScreenFadeOut(500).join()")
			Client.doScreenFadeOut(500).join()
			Client.setEntityCoordsNoOffset(Client.getPlayerPed(), 0, 0, 0, zAxis = true)
			val playerId = Client.getPlayerId()

			freezePlayer(playerId, true)

			modelHash?.let {
				Console.debug("withTimeout(5_000) { Client.requestModel(it).join() }")
				withTimeout(5_000) { Client.requestModel(it).join() }//todo test

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

			Console.debug("withTimeout(5_000) { Client.doScreenFadeIn(500).join() }")
			withTimeout(5_000) { Client.doScreenFadeIn(500).join() }//todo test

			freezePlayer(playerId, false)

			UEvent.emit(PlayerSpawnedEvent())
		} catch (exception: Throwable) {
			Console.error(exception.message)
		}
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

