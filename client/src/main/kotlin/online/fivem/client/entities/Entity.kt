package online.fivem.client.entities

import online.fivem.client.gtav.Client
import online.fivem.common.entities.Coordinates
import online.fivem.common.entities.CoordinatesX

abstract class Entity(val entity: Int) {

	val model = Client.getEntityModel(entity)

	var health: Int
		get() = Client.getEntityHealth(entity)
		set(value) = Client.setEntityHealth(entity, value)

	var heading: Float
		get() = Client.getEntityHeading(entity)
		set(value) = Client.setEntityHeading(entity, value)

	var coordinates: Coordinates
		get() = Client.getEntityCoords(entity)
		set(value) {
			Client.setEntityCoordsNoOffset(entity, value.x, value.y, value.z)
			if (value is CoordinatesX) {
				heading = value.rotation
			}
		}

	fun getSpeed() = Client.getEntitySpeed(entity)

	fun getSpeedVector(relative: Boolean) = Client.getEntitySpeedVector(entity, relative)

	fun getRoll() = Client.getEntityRoll(entity)
}