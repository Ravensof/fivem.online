package online.fivem.client.common

import online.fivem.client.entities.Ped
import online.fivem.client.entities.Vehicle
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Client.doesEntityExist
import online.fivem.common.common.EntityId

object GlobalCache {

	val player by lazy { Player(Client.getPlayerId()) }

	private val vehicles = mutableMapOf<EntityId, Vehicle>()
	private val peds = mutableMapOf<EntityId, Ped>()

	fun getVehicle(entity: EntityId): Vehicle? {
		if (!doesEntityExist(entity)) {
			vehicles.remove(entity)

			return null
		}

		return vehicles[entity]
	}

	fun putVehicle(vehicle: Vehicle) {
		vehicles[vehicle.entity] = vehicle

		vehicles.forEach {
			if (!doesEntityExist(it.key)) {
				vehicles.remove(it.key)
			}
		}
	}

	fun getPed(entity: EntityId): Ped? {
		if (!doesEntityExist(entity)) {
			peds.remove(entity)

			return null
		}

		return peds[entity]
	}

	fun putPed(ped: Ped) {
		peds[ped.entity] = ped

		peds.forEach {
			if (!doesEntityExist(it.key)) {
				vehicles.remove(it.key)
			}
		}
	}
}