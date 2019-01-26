package online.fivem.client.common

import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Handle
import online.fivem.common.common.Entity

class PickupsIterator : Iterator<Entity> {

	private var currentEntity: Entity
	private val handle: Handle

	init {
		val findHandle = Client.findFirstPickup()
		handle = findHandle.first
		currentEntity = findHandle.second
	}

	override fun hasNext(): Boolean {
		return currentEntity != -1
	}

	override fun next(): Entity {
		val entity = currentEntity
		val nextResult = Client.findNextPickup(handle)
		currentEntity = nextResult.second
		return entity
	}

	fun close() {
		Client.endFindPickup(handle)
	}
}