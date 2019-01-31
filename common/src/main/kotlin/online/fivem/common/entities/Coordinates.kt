package online.fivem.common.entities

open class Coordinates(
	open val x: Float,
	open val y: Float,
	open val z: Float
) {
	override fun equals(other: Any?): Boolean {
		if (other !is Coordinates) return false

		return x == other.x && y == other.y && z == other.z
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		result = 31 * result + z.hashCode()
		return result
	}
}