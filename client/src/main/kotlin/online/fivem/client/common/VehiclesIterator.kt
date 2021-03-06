package online.fivem.client.common

import online.fivem.Natives
import online.fivem.common.common.EntityId
import online.fivem.common.common.Handle

class VehiclesIterator : IObjectIterator<EntityId> {

	private var currentEntity: EntityId = -1
	private var handle: Handle = 0

	init {
		val findHandle = Natives.findFirstVehicle()
		handle = findHandle.first
		currentEntity = findHandle.second
	}

	override fun hasNext(): Boolean {
		return currentEntity != -1
	}

	override fun next(): EntityId {
		val entity = currentEntity
		val nextResult = Natives.findNextVehicle(handle)
		currentEntity = nextResult.second
		return entity
	}

	override fun close() {
		Natives.endFindVehicle(handle)
	}
}