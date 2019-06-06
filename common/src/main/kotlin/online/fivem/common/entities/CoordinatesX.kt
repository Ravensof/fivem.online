package online.fivem.common.entities

import kotlinx.serialization.Serializable

@Serializable
class CoordinatesX(
	val x: Float,
	val y: Float,
	val z: Float,
	val rotation: Float
) {
	constructor(coordinates: Coordinates, rotation: Float) : this(
		coordinates.x,
		coordinates.y,
		coordinates.z,
		rotation
	)

	override fun equals(other: Any?): Boolean {
		if (other !is CoordinatesX) return false

		return super.equals(other) && rotation == other.rotation
	}

	override fun hashCode(): Int {
		var result = super.hashCode()
		result = 31 * result + rotation.hashCode()
		return result
	}

	fun toCoordinates() = Coordinates(x, y, z)

	companion object {
		val ZERO = CoordinatesX(0f, 0f, 0f, 0f)
	}
}