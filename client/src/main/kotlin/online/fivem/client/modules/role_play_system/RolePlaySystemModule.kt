package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import online.fivem.client.entities.Ped
import online.fivem.client.events.PlayerVehicleSeatEvent
import online.fivem.client.events.PlayersPedChangedEvent
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Stack
import online.fivem.common.common.UEvent
import kotlin.coroutines.CoroutineContext

class RolePlaySystemModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = SupervisorJob()

	private val tickExecutor by moduleLoader.onReady<TickExecutorModule>()

	override fun onInit() {
		UEvent.on<PlayerVehicleSeatEvent.Join.Passenger> {
			if (it.seatIndex == 0) {
				disableSeatShuffling(true)
			}
		}
		UEvent.on<PlayerVehicleSeatEvent.Left> { disableSeatShuffling(false) }
		UEvent.on<PlayersPedChangedEvent> { onPedChanged(it.ped) }
	}

	override fun onStart(): Job? {
		Client.setPlayerHealthRechargeMultiplier(Client.getPlayerId(), 0f)

		return super.onStart()
	}

	private fun onPedChanged(ped: Ped) {
		Client.setPedCanRagdollFromPlayerImpact(ped.entity, true)
	}

	private var seatShuffling = Stack.UNDEFINED_INDEX

	private fun disableSeatShuffling(disable: Boolean) {
		val playerPed = Client.getPlayerPedId()
		val vehicle = Client.getVehiclePedIsIn(playerPed, false) ?: return

		tickExecutor.remove(seatShuffling)
		if (disable) {
			seatShuffling = tickExecutor.add {
				if (Client.getIsTaskActive(playerPed, 165)) {
					Client.setPedIntoVehicle(playerPed, vehicle, 0)
				}
			}
		}
	}
}