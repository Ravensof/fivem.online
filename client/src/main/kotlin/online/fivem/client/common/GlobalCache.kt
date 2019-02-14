package online.fivem.client.common

import online.fivem.client.entities.Ped
import online.fivem.client.entities.Vehicle
import online.fivem.client.gtav.Client
import online.fivem.common.common.Entity

object GlobalCache {
	private val vehicles = mutableMapOf<Entity, Vehicle>()
	private val peds = mutableMapOf<Entity, Ped>()

	fun getVehicle(entity: Entity): Vehicle? {
		if (!Client.doesEntityExist(entity)) {
			vehicles.remove(entity)

			return null
		}

		return vehicles[entity]
	}

	fun putVehicle(vehicle: Vehicle) {
		vehicles[vehicle.entity] = vehicle
	}

	fun getPed(entity: Entity): Ped? {
		if (!Client.doesEntityExist(entity)) {
			peds.remove(entity)

			return null
		}

		return peds[entity]
	}

	fun putPed(ped: Ped) {
		peds[ped.entity] = ped
	}
}