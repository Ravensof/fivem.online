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
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.common.extensions.onNull

class SpawnManagerModule(
	private val bufferedActionsModule: BufferedActionsModule
) : AbstractClientModule() {


//	private fun vehicleSpawn(event: SpawnVehicleEvent) {
//
//		launch {
//			val vehicle = withTimeout(5_000) { Client.createVehicle(event.vehicleModel, event.coordinatesX).await() }
//
//			Client.setVehicleOilLevel(vehicle, event.vehicleId)
//			Client.setVehicleWheelHealth(vehicle)
//		}
//	}

	override fun onStart() = launch {
		bufferedActionsModule.waitForStart()
	}

	fun spawnPlayerJob(event: SpawnPlayerEvent): Job {

		val job = async(start = CoroutineStart.LAZY) {
			player.ped.coordinatesX = CoordinatesX.ZERO

			freezePlayer(player, true)

			event.pedModel?.let {
				player.setModel(it)
			}

			val ped = player.ped

			Client.requestCollisionAtCoordinates(event.coordinatesX.toCoordinates())

			bufferedActionsModule.coordinatesLocker.lock(this@SpawnManagerModule)
			ped.coordinatesX = event.coordinatesX
			delay(1_000)
			bufferedActionsModule.coordinatesLocker.unlock(this@SpawnManagerModule)

			withTimeoutOrNull(10_000) {
				while (!Client.hasCollisionLoadedAroundEntity(ped.entity)) {
					delay(100)
				}
			}.onNull {
				Console.warn("failed to request collision at spawn coordinates")
			}

			Client.networkResurrectLocalPlayer(event.coordinatesX)
			ped.clearTasksImmediately()
			ped.removeAllWeapons()
			player.clearWantedLevel()
			player.ped.health = event.health
			player.ped.armour = event.armour

			event.weapons.forEach {
				player.ped.giveWeapon(it.key, it.value)
			}

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

	private suspend fun freezePlayer(player: Player, freeze: Boolean) {

		val ped = player.ped

		if (!freeze) {
			bufferedActionsModule.unLockControl(this)

			ped.isVisible = true
			ped.isInAVehicle()

			if (!ped.isInAVehicle()) {
				ped.setCollision(true)
			}

			ped.freezePosition(false)
			player.isInvincible = false

		} else {
			bufferedActionsModule.lockControl(this@SpawnManagerModule)

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

