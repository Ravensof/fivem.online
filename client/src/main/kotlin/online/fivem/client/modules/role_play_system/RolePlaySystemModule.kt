package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.Job
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.events.PlayerVehicleSeatEvent
import online.fivem.client.events.PlayersPedChangedEvent
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.common.Event
import online.fivem.common.common.Stack
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.sync.RolePlaySystemSaveEvent

class RolePlaySystemModule : AbstractClientModule() {

	private val tickExecutor by moduleLoader.delegate<TickExecutorModule>()

	override suspend fun onInit() {
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

	override fun onStart(): Job? {
		Client.setPlayerHealthRechargeMultiplier(Client.getPlayerId(), 0f)

		return super.onStart()
	}

	override suspend fun onSave(): RolePlaySystemSaveEvent {

		val playerPed = player.ped

		return RolePlaySystemSaveEvent(
			CoordinatesX(
				playerPed.coordinates,
				playerPed.heading
			)
		)
	}

	private fun onPedChanged(ped: Ped) {
		Client.setPedCanRagdollFromPlayerImpact(ped.entity, true)
	}

	private var seatShuffling = Stack.UNDEFINED_INDEX

	private fun disableSeatShuffling(disable: Boolean) {
		val vehicle = player.ped.getVehicleIsIn(false) ?: return

		tickExecutor.remove(seatShuffling)
		if (disable) {
			seatShuffling = tickExecutor.add {
				if (Client.getIsTaskActive(player.ped.entity, 165)) {
					Client.setPedIntoVehicle(player.ped.entity, vehicle.entity, 0)
				}
			}
		}
	}
}