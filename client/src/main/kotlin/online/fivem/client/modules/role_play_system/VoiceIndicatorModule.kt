package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import online.fivem.Natives
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.extensions.distance
import online.fivem.client.modules.basics.StateRepositoryModule
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.entities.RGBA
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.orZero
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.repeatJob
import online.fivem.enums.MarkerType

class VoiceIndicatorModule(
	private val tickExecutorModule: TickExecutorModule,
	private val stateRepositoryModule: StateRepositoryModule
) : AbstractClientModule() {

	override fun onStartAsync() = async {
		tickExecutorModule.waitForStart()

		startDrawingIndicator()
	}

	private fun startDrawingIndicator() = launch {

		val coordinatesChannel = stateRepositoryModule.coordinatesX.openSubscription()

		var myCoordinates = coordinatesChannel.receive()
		var pedIndicators: Map<Int, Boolean>

		launch {
			coordinatesChannel.forEach {
				myCoordinates = it
			}
		}

		repeatJob(100) {

			pedIndicators = stateRepositoryModule.talkingPlayers
				.receiveAndCancel()
				.associate { playerId ->

					val playerPed = Natives.getPlayerPed(playerId).orZero()

					playerPed to (
							Natives.isEntityVisible(playerPed)
									&& myCoordinates.toCoordinates().distance(Natives.getEntityCoords(playerPed)) <= DRAW_DISTANCE_M
							)
				}

			if (pedIndicators.isNotEmpty()) {
				tickExecutorModule.add(this) {
					pedIndicators.forEach {
						if (!it.value) return@forEach

						val coordinates = Natives.getEntityCoords(it.key)

						val color = if (it.value) {
							RGBA(160, 205, 105, 105)
						} else {
							RGBA(239, 239, 239, 50)
						}

						Natives.drawMarker(
							MarkerType.HORIZONTAL_CIRCLE_SKINNY.code,
							coordinates.x,
							coordinates.y,
							coordinates.z - MARKER_POSITION,
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
		const val MARKER_POSITION = 0.95f
	}
}