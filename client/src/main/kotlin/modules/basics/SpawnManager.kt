package online.fivem.client.modules.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.PlayerSpawnedEvent

class SpawnManager : AbstractModule() {

	override fun start(): Job? {
		return super.start()
	}

	companion object {

		fun spawnPlayer(coordinatesX: CoordinatesX, modelHash: Int?, callback: () -> Unit = {}) {
			GlobalScope.launch {
				Client.doScreenFadeOut(500)

				while (Client.isScreenFadingOut()) {
					delay(100)
				}

				val playerId = Client.getPlayerId()

				freezePlayer(playerId, true)

				modelHash?.let {
					Client.requestModel(it)

					while (!Client.hasModelLoaded(it)) {
						delay(100)
					}

					Client.setPlayerModel(playerId, it)
					Client.setModelAsNoLongerNeeded(it)
				}

				Client.requestCollisionAtCoordinates(coordinatesX)

				val ped = Client.getPlayerPed() ?: 0

				Client.setEntityCoordsNoOffset(ped, coordinatesX.x, coordinatesX.y, coordinatesX.z, zAxis = true)
				Client.networkResurrectLocalPlayer(coordinatesX)
				Client.clearPedTasksImmediately(ped)
				Client.removeAllPedWeapons(ped)
				Client.clearPlayerWantedLevel(playerId)

				while (!Client.hasCollisionLoadedAroundEntity(ped)) {
					delay(100)
				}

				Client.shutdownLoadingScreen()
				Client.doScreenFadeIn(500)

				while (Client.isScreenFadingIn()) {
					delay(100)
				}

				freezePlayer(playerId, false)

				UEvent.emit(PlayerSpawnedEvent())

				callback()
			}
		}

		private fun freezePlayer(playerSrc: Int, freeze: Boolean) {
			Client.setPlayerControl(playerSrc, !freeze, 0)

			val ped = Client.getPlayerPed() ?: return

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
}

