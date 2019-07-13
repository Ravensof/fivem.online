package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import online.fivem.Natives
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.extensions.distance
import online.fivem.common.GlobalConfig
import online.fivem.common.common.EntityId
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.repeatJob

class VoiceTransmissionModule(
	private val stateRepositoryModule: StateRepositoryModule
) : AbstractClientModule() {

	private var checkJob: Job? = null


	override suspend fun onInit() {
		Natives.networkSetTalkerProximity(-1000)
	}

	override fun onStartAsync() = async {
		stateRepositoryModule.waitForStart()

		if (GlobalConfig.IS_ONE_SYNC_ENABLED) {
			startCheckingSpeakers()
		} else {
			subscribeOnIsPlayerTalking()
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

	private fun startCheckingSpeakers() = launch {
		for (talkingPlayers in stateRepositoryModule.talkingPlayers.openSubscription()) {
			talkingPlayers
				.filter { it != player.id }
				.forEach { anotherPlayer ->
					val pedId = Natives.getPlayerPed(anotherPlayer) ?: return@forEach

					Natives.networkOverrideReceiveRestrictions(
						anotherPlayer,
						toggle = Ped.newInstance(pedId)
							.isAbleToTalkTo(player.ped.entityId)
					)
				}
		}
	}

	private fun startTalking() = repeatJob(100) {

		val loudness = 1f //player.networkGetLoudness() * 10

		stateRepositoryModule.activePlayers
			.receiveAndCancel()
			.filter { it != player.id && Natives.networkIsPlayerTalking(it) }
			.forEach { anotherPlayer ->

				val anotherPlayerPedId = Natives.getPlayerPed(anotherPlayer) ?: return@forEach

				Natives.networkOverrideSendRestrictions(
					anotherPlayer,
					player.ped.isAbleToTalkTo(anotherPlayerPedId, loudness)
				)
			}
	}.also {
		checkJob?.cancel()
		checkJob = it
	}

	private fun Ped.isAbleToTalkTo(anotherPlayerPedId: EntityId, loudness: Float = 1f): Boolean {

		val anotherPlayerCoordinates = Natives.getEntityCoords(anotherPlayerPedId)

		val distance = coordinates.distance(anotherPlayerCoordinates)

		return when {

			isInAVehicle() -> distance <= DISTANCE_HEARING_IN_VEHICLE * loudness

			distance <= DISTANCE_HEARING_THROUGH_WALLS * loudness -> true

			distance <= DISTANCE_HEARING_CLEAR && Natives.hasEntityClearLosToEntity(
				entityId,
				anotherPlayerPedId
			) -> true

			distance <= DISTANCE_HEARING_CLEAR_IN_FRONT * loudness && Natives.hasEntityClearLosToEntityInFront(
				entityId,
				anotherPlayerPedId
			) -> true

			else -> false
		}
	}

	private fun stopTalking() = checkJob?.cancel()

	private companion object {
		const val DISTANCE_HEARING_THROUGH_WALLS = 2.0
		const val DISTANCE_HEARING_IN_VEHICLE = 4.0
		const val DISTANCE_HEARING_CLEAR = 15.0
		const val DISTANCE_HEARING_CLEAR_IN_FRONT = 25.0
	}
}