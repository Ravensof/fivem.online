package online.fivem.client.common

import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Handle
import online.fivem.common.common.Entity

class VehiclesIterator : Iterator<Entity> {

	private var currentEntity: Entity = -1
	private var handle: Handle = 0

	init {
		start()
	}

	override fun hasNext(): Boolean {
		return currentEntity != -1
	}

	override fun next(): Entity {
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