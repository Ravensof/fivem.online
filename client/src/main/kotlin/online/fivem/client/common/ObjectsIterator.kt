package online.fivem.client.common

import online.fivem.client.gtav.Client
import online.fivem.common.common.EntityId
import online.fivem.common.common.Handle

class ObjectsIterator : IObjectIterator<EntityId> {

	private var currentEntity: EntityId
	private val handle: Handle

	init {
		val findHandle = Client.findFirstObject()
		handle = findHandle.first
		currentEntity = findHandle.second
	}

	override fun hasNext(): Boolean {
		return currentEntity != -1
	}

	override fun next(): EntityId {
		val entity = currentEntity
		val nextResult = Client.findNextObject(handle)
		currentEntity = nextResult.second
		return entity
	}

	override fun close() {
		Client.endFindObject(handle)
	}
}