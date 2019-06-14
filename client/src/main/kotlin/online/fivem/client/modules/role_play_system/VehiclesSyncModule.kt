package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.VehiclesIterator
import online.fivem.client.entities.Vehicle
import online.fivem.client.gtav.Client
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.SpawnVehiclesCommand
import online.fivem.common.events.net.VehiclesSpawnedEvent
import online.fivem.common.events.net.sync.VehiclesSyncClientEvent

class VehiclesSyncModule(

) : AbstractClientModule() {

	override fun onStart(): Job? {

		ServerEvent.on<SpawnVehiclesCommand> { onVehiclesSpawnEvent(it.vehicles) }

		return super.onStart()
	}

	override fun onSaveState(container: ClientSideSynchronizationEvent) = launch {

		val vehicles = mutableListOf<VehiclesSyncClientEvent.Vehicle>()

		VehiclesIterator()
			.filter { Client.isVehiclePreviouslyOwnedByPlayer(it) }
			.forEach {

				val vehicle = Vehicle.newInstance(it)

				vehicles += VehiclesSyncClientEvent.Vehicle(
					networkId = vehicle.networkId,
					coordinatesX = vehicle.coordinatesX
				)
			}

		container.vehiclesSyncClientEvent = VehiclesSyncClientEvent(
			vehicles = vehicles
		)
	}

	private fun onVehiclesSpawnEvent(vehicles: List<SpawnVehiclesCommand.Vehicle>) = launch {

		val ids = mutableMapOf<Int, Int>()

		vehicles.forEach {
			val vehicle = Vehicle.create(
				vehicleModel = it.modelHash,
				coordinatesX = it.coordinatesX
			)

			ids[it.id] = vehicle.networkId
		}

		ServerEvent.emit(
			VehiclesSpawnedEvent(
				ids = ids
			)
		)
	}

}