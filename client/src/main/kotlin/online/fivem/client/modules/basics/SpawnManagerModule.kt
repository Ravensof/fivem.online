package online.fivem.client.modules.basics

import kotlinx.coroutines.*
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.common.Player
import online.fivem.client.events.PlayerSpawnProcess
import online.fivem.client.extensions.networkResurrectLocalPlayer
import online.fivem.client.extensions.requestCollisionAtCoordinates
import online.fivem.client.gtav.Client
import online.fivem.common.common.Console
import online.fivem.common.common.Event
import online.fivem.common.common.Stack
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.extensions.onNull

class SpawnManagerModule : AbstractClientModule() {

	private val api by moduleLoader.delegate<API>()

//	private fun vehicleSpawn(event: SpawnVehicleEvent) {
//
//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//			Client.setVehicleWheelHealth(vehicle)
//		}
//	}

	fun spawnPlayerJob(coordinatesX: CoordinatesX, modelHash: Int?): Job {

		val job = async(start = CoroutineStart.LAZY) {
			player.ped.coordinates = CoordinatesX.ZERO

			freezePlayer(player, true)

			modelHash?.let {
				player.setModel(it)
			}

			val ped = player.ped

			Client.requestCollisionAtCoordinates(coordinatesX)
			ped.coordinates = coordinatesX

			withTimeoutOrNull(10_000) {
				while (!Client.hasCollisionLoadedAroundEntity(ped.entity)) {
					delay(100)
				}
			}.onNull {
				Console.warn("failed to request collision at spawn coordinates")
			}

			Client.networkResurrectLocalPlayer(coordinatesX)
			ped.clearTasksImmediately()
			ped.removeAllWeapons()
			player.clearWantedLevel()

			freezePlayer(player, false)

			PlayerSpawnProcess.FinishedEvent()
		}

		Event.emitAsync(
			PlayerSpawnProcess(job) {
				Event.emitAsync(it)
			}
		)

		return job
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

