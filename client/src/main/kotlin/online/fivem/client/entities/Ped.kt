package online.fivem.client.entities

import online.fivem.client.gtav.Client
import online.fivem.common.common.Entity

class Ped(
	val entity: Entity
) {
	init {
		if (Client.doesEntityExist(entity)) throw PedDoesntExistsException()
	}

	class PedDoesntExistsException : Exception()

	companion object {
		fun fromEntity(list: List<Entity>): List<Ped> {
			return list.map { Ped(it) }
		}
	}
}