package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.events.PlayersPedChangedEvent
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.BufferedActionsModule
import online.fivem.client.modules.basics.StateRepositoryModule
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.client.modules.role_play_system.vehicle.VehicleModule
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.sync.RolePlaySystemSaveEvent

class RolePlaySystemModule(
	private val bufferedActionsModule: BufferedActionsModule,
	private val tickExecutorModule: TickExecutorModule,
	private val stateRepositoryModule: StateRepositoryModule
) : AbstractClientModule() {

	init {
		Client.setPlayerHealthRechargeMultiplier(Client.getPlayerId(), 0f)
	}

	override suspend fun onInit() {
		moduleLoader.add(
			VehicleModule(
				tickExecutorModule = tickExecutorModule
			)
		)

		moduleLoader.add(VehiclesSyncModule())
		moduleLoader.add(BlackOutModule(bufferedActionsModule))
		moduleLoader.add(
			VoiceIndicatorModule(
				tickExecutorModule = tickExecutorModule,
				stateRepositoryModule = stateRepositoryModule
			)
		)
	}

	override fun onStartAsync() = async {
		Event.on<PlayersPedChangedEvent> { onPedChanged(it.ped) }
	}

	override fun onSaveState(container: ClientSideSynchronizationEvent): Job? {

		val playerPed = player.ped

		container.rolePlaySystem = RolePlaySystemSaveEvent(
			coordinatesX = CoordinatesX(
				playerPed.coordinates,
				playerPed.heading
			),

			health = playerPed.health,

			armour = playerPed.armour,

			weapons = playerPed.getWeapons().associate { it.weapon.code to it.ammo }

		)

		return super.onSaveState(container)
	}

	private fun onPedChanged(ped: Ped) {
		Client.setPedCanRagdollFromPlayerImpact(ped.entity, true)
	}
}