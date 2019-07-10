package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.extensions.distance
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.enums.MarkerType
import online.fivem.client.modules.basics.StateRepositoryModule
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.entities.RGBA
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.orZero
import online.fivem.common.extensions.repeatJob

class VoiceIndicatorModule(
	private val tickExecutorModule: TickExecutorModule,
	private val stateRepositoryModule: StateRepositoryModule
) : AbstractClientModule() {

	override fun onStartAsync() = async {
		tickExecutorModule.waitForStart()

		startDrawingIndicator()
	}

	private fun startDrawingIndicator() = launch {
		val takeaway = 0.95f

		val coordinatesChannel = stateRepositoryModule.coordinatesX.openSubscription()

		var myCoordinates = coordinatesChannel.receive()
		var pedIndicators: Map<Int, Boolean>

		launch {
			coordinatesChannel.forEach {
				myCoordinates = it
			}
		}

		repeatJob(100) {

			pedIndicators = Client.getActivePlayers().associate { playerId ->
				val playerPed = Client.getPlayerPed(playerId).orZero()

				playerPed to (
						Client.networkIsPlayerTalking(playerId)
								&& Client.isEntityVisible(playerPed)
								&& myCoordinates.toCoordinates().distance(Client.getEntityCoords(playerPed)) <= DRAW_DISTANCE_M
						)
			}

			if (pedIndicators.isNotEmpty()) {
				tickExecutorModule.add(this) {
					pedIndicators.forEach {
						if (!it.value) return@forEach

						val coordinates = Client.getEntityCoords(it.key)

						val color = if (it.value) {
							RGBA(160, 205, 105, 105)
						} else {
							RGBA(239, 239, 239, 50)
						}

						Client.drawMarker(
							MarkerType.HORIZONTAL_CIRCLE_SKINNY.code,
							coordinates.x,
							coordinates.y,
							coordinates.z - takeaway,
							rotX = 36f,
							red = color.red,
							green = color.green,
							blue = color.blue,
							alpha = color.alpha
						)
					}
				}
			}
		}
	}

	private companion object {
		const val DRAW_DISTANCE_M = 10
	}
}