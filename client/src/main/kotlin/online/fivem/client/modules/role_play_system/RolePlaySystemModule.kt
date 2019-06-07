package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.events.PlayerVehicleSeatEvent
import online.fivem.client.events.PlayersPedChangedEvent
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.common.Event
import online.fivem.common.common.generateLong
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.sync.RolePlaySystemSaveEvent

class RolePlaySystemModule(
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	override suspend fun onInit() {
		Client.setPlayerHealthRechargeMultiplier(Client.getPlayerId(), 0f)

		Event.apply {
			on<PlayerVehicleSeatEvent.Join.Passenger> {
				if (it.seatIndex == 0) {
					disableSeatShuffling(true)
				}
			}
			on<PlayerVehicleSeatEvent.Left> { disableSeatShuffling(false) }
			on<PlayersPedChangedEvent> { onPedChanged(it.ped) }
		}
	}

	override fun onStart() = launch {
		tickExecutorModule.waitForStart()
	}

	override fun onSync(exportObject: ClientSideSynchronizationEvent): Job? {

		val playerPed = player.ped

		exportObject.rolePlaySystem = RolePlaySystemSaveEvent(
			CoordinatesX(
				playerPed.coordinates,
				playerPed.heading
			)
		)

		return super.onSync(exportObject)
	}

	private fun onPedChanged(ped: Ped) {
		Client.setPedCanRagdollFromPlayerImpact(ped.entity, true)
	}

	private val seatShuffling = generateLong()

	private fun disableSeatShuffling(disable: Boolean) {
		val vehicle = player.ped.getVehicleIsIn(false) ?: return


		if (disable) {
			tickExecutorModule.add(seatShuffling) {
				if (Client.getIsTaskActive(player.ped.entity, 165)) {
					Client.setPedIntoVehicle(player.ped.entity, vehicle.entity, 0)
				}
			}
		} else {
			tickExecutorModule.remove(seatShuffling)
		}
	}
}