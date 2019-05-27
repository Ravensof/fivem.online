package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.events.PlayersPedChangedEvent
import online.fivem.client.extensions.distance
import online.fivem.client.extensions.getPlayersOnline
import online.fivem.client.extensions.isControlPressed
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Client.getEntityCoords
import online.fivem.client.gtav.Client.getPlayerPed
import online.fivem.client.gtav.Client.hasEntityClearLosToEntity
import online.fivem.common.common.Event
import online.fivem.common.extensions.repeatJob
import online.fivem.common.gtav.NativeControls

class VoiceTransmissionModule : AbstractClientModule() {

	var playerPed: Ped? = null

	override suspend fun onInit() {
		Event.apply {
			on<PlayersPedChangedEvent> { playerPed = it.ped }
		}
		super.onInit()
	}

	override fun onStart(): Job? {

		Client.networkSetTalkerProximity(-1000)
//		Client.networkIsPlayerTalking()//если говорит, делать canPedSeePed()
//		Client.hasEntityClearLosToEntityInFront()
//		Client.networkOverrideReceiveRestrictions()
//		Client.networkOverrideSendRestrictions()

//		Client.canPedSeePed()

		repeatJob(100) {

			if (Client.networkIsPlayerTalking(player.id) || NativeControls.Keys.PUSH_TO_TALK.isControlPressed()) return@repeatJob

			val myCoordinates = player.ped.coordinates

			Client.getPlayersOnline().forEach { anotherPlayer ->
				if (anotherPlayer == player.id) return@forEach

				val anotherPlayerPed = getPlayerPed(anotherPlayer) ?: return@forEach
				val anotherPlayerCoordinates = getEntityCoords(Client.getPlayerPedId())

				val distance = myCoordinates.distance(anotherPlayerCoordinates)

				Client.networkOverrideSendRestrictions(
					anotherPlayer,
					when {
						distance <= DISTANCE_HEARING_THROUGH_WALLS -> true

						distance <= DISTANCE_HEARING_CLEAR && hasEntityClearLosToEntity(
							player.ped.entity,
							anotherPlayerPed
						) -> true

//						Client.hasEntityClearLosToEntityInFront(myPed, anotherPlayerPed) -> true

						else -> false
					}
				)
			}
		}

		return super.onStart()
	}

	companion object {
		const val DISTANCE_HEARING_THROUGH_WALLS = 2.0
		const val DISTANCE_HEARING_CLEAR = 15.0
	}
}