package online.fivem.client.entities

import online.fivem.client.common.GlobalCache
import online.fivem.client.extensions.isPedAtGetInAnyVehicle
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Client.isPedInAnyVehicle
import online.fivem.common.common.EntityId

class Ped private constructor(
	entity: EntityId
) : Entity(entity) {
	init {
		if (!Client.doesEntityExist(entity)) throw PedDoesntExistsException("ped $entity doesnt exists")
	}

	fun isAtGetInAVehicle() = Client.isPedAtGetInAnyVehicle(entity)

	fun isInAVehicle() = isPedInAnyVehicle(entity, false)

	fun getVehicleIsUsing(): Vehicle? {
		Client.getVehiclePedIsUsing(entity)?.let { entity ->
			return Vehicle.newInstance(entity)
		}

		return null
	}

	fun getVehicleIsIn(lastVehicle: Boolean = false): Vehicle? {
		return Client.getVehiclePedIsIn(entity, lastVehicle)?.let { Vehicle.newInstance(it) }
	}

	fun clearTasksImmediately() = Client.clearPedTasksImmediately(entity)

	fun removeAllWeapons() = Client.removeAllPedWeapons(entity)

	class PedDoesntExistsException(message: String) : Exception(message)

	companion object {

		fun newInstance(entity: EntityId): Ped {
			GlobalCache.getPed(entity)?.let {
				return it
			}

			val ped = Ped(entity)

			GlobalCache.putPed(ped)

			return ped
		}
	}
}