package online.fivem.client.modules.role_play_system.vehicle

import kotlinx.coroutines.async
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache
import online.fivem.client.events.PlayerVehicleSeatEvent
import online.fivem.client.modules.basics.StateRepositoryModule
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.common.Event
import online.fivem.common.common.generateLong
import online.fivem.common.gtav.NativeTask

class VehicleModule(
	private val tickExecutorModule: TickExecutorModule,
	private val stateRepositoryModule: StateRepositoryModule
) : AbstractClientModule() {

	private val seatShuffling = generateLong()

	override suspend fun onInit() {
		moduleLoader.apply {
			add(InternetRadioModule(stateRepositoryModule))
			add(SpeedometerModule())
			add(RealisticFailureModule(tickExecutorModule))
		}

		Event.apply {
			on<PlayerVehicleSeatEvent.Join.Passenger> {
				if (it.seatIndex == 0) {
					disableSeatShuffling(true)
				}
			}
			on<PlayerVehicleSeatEvent.Left> { disableSeatShuffling(false) }
		}
	}

	override fun onStartAsync() = async {
		tickExecutorModule.waitForStart()
	}

	private fun disableSeatShuffling(disable: Boolean) {
		val vehicle = GlobalCache.player.ped.getVehicleIsIn(false) ?: return

		if (disable) {
			tickExecutorModule.add(seatShuffling) {
				if (GlobalCache.player.ped.isTaskActive(NativeTask.InVehicleSeatShuffle)) {
					GlobalCache.player.ped.setIntoVehicle(vehicle.entityId, 0)
				}
			}
		} else {
			tickExecutorModule.remove(seatShuffling)
		}
	}
}