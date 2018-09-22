package universal.struct

data class Coords(
		val x: Float,
		val y: Float,
		val z: Float
) {
	fun toArray(): Array<Float> {
		return arrayOf(x, y, z)
	}
}