package online.fivem.client.modules.basics

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import online.fivem.Natives
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.common.Player
import online.fivem.client.events.PlayerPedSpawnedEvent
import online.fivem.client.extensions.networkResurrectLocalPlayer
import online.fivem.client.extensions.requestCollisionAtCoordinates
import online.fivem.common.common.Console
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.common.extensions.onNull

class SpawnManagerModule(
	private val bufferedActionsModule: BufferedActionsModule
) : AbstractClientModule() {

	override fun onStartAsync() = async {
		bufferedActionsModule.waitForStart()
	}

	fun spawnPlayerPed(event: SpawnPlayerEvent) = launch {

		bufferedActionsModule.coordinatesLocker.lock(this@SpawnManagerModule)

		player.ped.coordinatesX = CoordinatesX.ZERO

		freezePlayer(player)

		event.pedModel?.let {
			player.setModel(it)
		}

		val ped = player.ped

		Natives.requestCollisionAtCoordinates(event.coordinatesX.toCoordinates())

		ped.coordinatesX = event.coordinatesX

		withTimeoutOrNull(10_000) {
			while (!Natives.hasCollisionLoadedAroundEntity(ped.entityId)) {
				delay(100)
			}
		}.onNull {
			Console.warn("failed to request collision at spawn coordinates")
		}

		Natives.networkResurrectLocalPlayer(event.coordinatesX)
		ped.clearTasksImmediately()
		ped.removeAllWeapons()
		player.clearWantedLevel()

		player.ped.apply {

			health = event.health
			armour = event.armour

			event.weapons.forEach {
				giveWeapon(it.key, it.value)
			}
		}

		unfreezePlayer(player)
		bufferedActionsModule.coordinatesLocker.unlock(this@SpawnManagerModule)

		Event.emit(PlayerPedSpawnedEvent(ped))
	}

	private suspend fun freezePlayer(player: Player) {

		val ped = player.ped

		bufferedActionsModule.lockControl(this@SpawnManagerModule)

		ped.isVisible = false
		ped.setCollision(false)
		ped.freezePosition(true)
		player.isInvincible = true

		if (Natives.isPedFatallyInjured(ped.entityId)) {
			ped.clearTasksImmediately()
		}
	}

	private suspend fun unfreezePlayer(player: Player) {
		val ped = player.ped

		bufferedActionsModule.unLockControl(this)

		ped.isVisible = true
		ped.isInAVehicle()

		if (!ped.isInAVehicle()) {
			ped.setCollision(true)
		}

		ped.freezePosition(false)
		player.isInvincible = false
	}
}

