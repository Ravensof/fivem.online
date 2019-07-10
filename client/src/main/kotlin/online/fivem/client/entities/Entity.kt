package online.fivem.client.entities

import online.fivem.Natives
import online.fivem.common.entities.Coordinates
import online.fivem.common.entities.CoordinatesX

abstract class Entity(val entity: Int) {

	val model = Natives.getEntityModel(entity)

	var health: Int
		get() = Natives.getEntityHealth(entity)
		set(value) = Natives.setEntityHealth(entity, value)

	var heading: Float
		get() = Natives.getEntityHeading(entity)
		set(value) = Natives.setEntityHeading(entity, value)

	var coordinates: Coordinates
		get() = Natives.getEntityCoords(entity)
		set(value) {
			Natives.setEntityCoordsNoOffset(entity, value.x, value.y, value.z)
		}

	var coordinatesX: CoordinatesX
		get() = CoordinatesX(coordinates = Natives.getEntityCoords(entity), rotation = Natives.getEntityHeading(entity))
		set(value) {
			Natives.setEntityCoordsNoOffset(entity, value.x, value.y, value.z)
			Natives.setEntityHeading(entity, heading)
		}

	var isVisible: Boolean
		get() = Natives.isEntityVisible(entity)
		set(value) = Natives.setEntityVisible(entity, value)

	override fun equals(other: Any?): Boolean {
		return other is Entity && entity == other.entity && model == other.model
	}

	override fun hashCode(): Int {
		var result = entity
		result = 31 * result + model
		return result
	}

	fun getSpeed() = Natives.getEntitySpeed(entity)

	fun getSpeedVector(relative: Boolean) = Natives.getEntitySpeedVector(entity, relative)

	fun getRoll() = Natives.getEntityRoll(entity)

	fun freezePosition(freeze: Boolean) = Natives.freezeEntityPosition(entity, freeze)

	fun setCollision(toggle: Boolean, keepPhysics: Boolean = true) =
		Natives.setEntityCollision(entity, toggle, keepPhysics)

}