package online.fivem.common.extensions

import online.fivem.common.entities.Coordinates

fun Coordinates.toArray(): Array<Float> {
	return arrayOf(x, y, z)
}

fun Coordinates.distance(coordinates: Coordinates): Float {
	return kotlin.math.sqrt(
		(x - coordinates.x) * (x - coordinates.x) +
				(y - coordinates.y) * (y - coordinates.y) +
				(z - coordinates.z) * (z - coordinates.z)
	)
}