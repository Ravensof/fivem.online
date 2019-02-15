package online.fivem.client.common

import online.fivem.client.gtav.Client
import online.fivem.common.common.EntityId
import online.fivem.common.common.Handle

class VehiclesIterator : Iterator<EntityId> {

	private var currentEntity: EntityId = -1
	private var handle: Handle = 0

	init {
		start()
	}

	override fun hasNext(): Boolean {
		return currentEntity != -1
	}

	override fun next(): EntityId {
		val entity = currentEntity
		val nextResult = Client.findNextVehicle(handle)
		currentEntity = nextResult.second
		return entity
	}

	fun close() {
		Client.endFindVehicle(handle)
	}

	fun start() {
		val findHandle = Client.findFirstVehicle()
		handle = findHandle.first
		currentEntity = findHandle.second
	}
}