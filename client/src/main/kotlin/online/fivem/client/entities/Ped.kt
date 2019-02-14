package online.fivem.client.entities

import online.fivem.client.common.GlobalCache
import online.fivem.client.extensions.isPedAtGetInAnyVehicle
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Client.getEntityHeading
import online.fivem.client.gtav.Client.getEntityHealth
import online.fivem.client.gtav.Client.isPedInAnyVehicle
import online.fivem.client.gtav.Client.setEntityHeading
import online.fivem.client.gtav.Client.setEntityHealth
import online.fivem.common.common.Entity

class Ped private constructor(
	val entity: Entity
) {
	init {
		if (Client.doesEntityExist(entity)) throw PedDoesntExistsException()
	}

	val isAtGetInAVehicle: Boolean get() = Client.isPedAtGetInAnyVehicle(entity)
	val isInAVehicle: Boolean get() = isPedInAnyVehicle(entity, false)

	var heading: Float
		get() = getEntityHeading(entity)
		set(value) = setEntityHeading(entity, value)

	var health: Int
		get() = getEntityHealth(entity)
		set(value) = setEntityHealth(entity, value)

	fun getVehicleIsUsing(): Vehicle? {
		Client.getVehiclePedIsUsing(entity)?.let { entity ->
			return Vehicle.newInstance(entity)
		}

		return null
	}

	fun getVehicleIsIn(lastVehicle: Boolean = false): Entity? {
		return Client.getVehiclePedIsIn(entity, lastVehicle)
	}

	class PedDoesntExistsException : Exception()

	companion object {

		fun newInstance(entity: Entity): Ped {
			GlobalCache.getPed(entity)?.let {
				return it
			}

			val ped = Ped(entity)

			GlobalCache.putPed(ped)

			return ped
		}

		fun fromEntity(list: List<Entity>): List<Ped> {
			return list.map { Ped(it) }
		}
	}
}