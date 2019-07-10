package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import online.fivem.Natives
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.extensions.distance
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.repeatJob

class VoiceTransmissionModule(
	private val stateRepositoryModule: StateRepositoryModule
) : AbstractClientModule() {

	var playerPed: Ped? = null
	//todo location_id

	private var checkJob: Job? = null

	init {
		Natives.networkSetTalkerProximity(-1000)
	}

	override fun onStartAsync() = async {
		stateRepositoryModule.waitForStart()

		subscribeOnPed()
		subscribeOnIsPlayerTalking()
	}

	private fun subscribeOnPed() = launch {
		stateRepositoryModule.playerPed.openSubscription().forEach {
			playerPed = it
		}
	}

	private fun subscribeOnIsPlayerTalking() = launch {
		stateRepositoryModule.isPlayerTalking.openSubscription().forEach { isTalking ->
			if (isTalking) {
				startTalking()
			} else {
				stopTalking()
			}
		}
	}

	private fun startTalking() = repeatJob(100) {

		val myCoordinates = player.ped.coordinates
		val loudness = 1f //player.networkGetLoudness() * 10

		Natives.getActivePlayers().forEach { anotherPlayer ->
			if (anotherPlayer == player.id) return@forEach

			val anotherPlayerPed = Natives.getPlayerPed(anotherPlayer) ?: return@forEach
			val anotherPlayerCoordinates = Natives.getEntityCoords(anotherPlayerPed)

			val distance = myCoordinates.distance(anotherPlayerCoordinates)

			val enable = when {

				player.ped.isInAVehicle() -> distance <= DISTANCE_HEARING_IN_VEHICLE * loudness

				distance <= DISTANCE_HEARING_THROUGH_WALLS * loudness -> true

				distance <= DISTANCE_HEARING_CLEAR && Natives.hasEntityClearLosToEntity(
					player.ped.entity,
					anotherPlayerPed
				) -> true

				distance <= DISTANCE_HEARING_CLEAR_IN_FRONT * loudness && Natives.hasEntityClearLosToEntityInFront(
					player.ped.entity,
					anotherPlayerPed
				) -> true

				else -> false
			}

			Natives.networkOverrideSendRestrictions(
				anotherPlayer,
				enable
			)
		}
	}.also {
		checkJob?.cancel()
		checkJob = it
	}

	private fun stopTalking() = checkJob?.cancel()

	private companion object {
		const val DISTANCE_HEARING_THROUGH_WALLS = 2.0
		const val DISTANCE_HEARING_IN_VEHICLE = 4.0
		const val DISTANCE_HEARING_CLEAR = 15.0
		const val DISTANCE_HEARING_CLEAR_IN_FRONT = 25.0
	}
}