package online.fivem.client.entities

import online.fivem.Natives
import online.fivem.common.common.EntityId
import online.fivem.common.entities.Coordinates
import online.fivem.common.entities.CoordinatesX

abstract class Entity(val entityId: EntityId) {

	val model = Natives.getEntityModel(entityId)

	var health: Int
		get() = Natives.getEntityHealth(entityId)
		set(value) = Natives.setEntityHealth(entityId, value)

	var heading: Float
		get() = Natives.getEntityHeading(entityId)
		set(value) = Natives.setEntityHeading(entityId, value)

	var coordinates: Coordinates
		get() = Natives.getEntityCoords(entityId)
		set(value) {
			Natives.setEntityCoordsNoOffset(entityId, value.x, value.y, value.z)
		}

	var coordinatesX: CoordinatesX
		get() = CoordinatesX(
			coordinates = Natives.getEntityCoords(entityId),
			rotation = Natives.getEntityHeading(entityId)
		)
		set(value) {
			Natives.setEntityCoordsNoOffset(entityId, value.x, value.y, value.z)
			Natives.setEntityHeading(entityId, heading)
		}

	var isVisible: Boolean
		get() = Natives.isEntityVisible(entityId)
		set(value) = Natives.setEntityVisible(entityId, value)

	override fun equals(other: Any?): Boolean {
		return other is Entity && entityId == other.entityId && model == other.model
	}

	override fun hashCode(): Int {
		var result = entityId
		result = 31 * result + model
		return result
	}

	fun getSpeed() = Natives.getEntitySpeed(entityId)

	fun getSpeedVector(relative: Boolean) = Natives.getEntitySpeedVector(entityId, relative)

	fun getRoll() = Natives.getEntityRoll(entityId)

	fun freezePosition(freeze: Boolean) = Natives.freezeEntityPosition(entityId, freeze)

	fun setCollision(toggle: Boolean, keepPhysics: Boolean = true) =
		Natives.setEntityCollision(entityId, toggle, keepPhysics)

}