package online.fivem.client.common

import online.fivem.client.gtav.Client
import online.fivem.common.common.Entity
import online.fivem.common.common.Handle

class ObjectsIterator : Iterator<Entity> {

	private var currentEntity: Entity
	private val handle: Handle

	init {
		val findHandle = Client.findFirstObject()
		handle = findHandle.first
		currentEntity = findHandle.second
	}

	override fun hasNext(): Boolean {
		return currentEntity != -1
	}

	override fun next(): Entity {
		val entity = currentEntity
		val nextResult = Client.findNextObject(handle)
		currentEntity = nextResult.second
		return entity
	}

	fun close() {
		Client.endFindObject(handle)
	}
}