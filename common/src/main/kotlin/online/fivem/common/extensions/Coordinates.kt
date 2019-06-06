package online.fivem.common.extensions

import online.fivem.common.entities.Coordinates
import online.fivem.common.entities.CoordinatesX

fun Coordinates.toArray(): Array<Number> {
	return arrayOf(x, y, z)
}

fun Coordinates.distance(coordinates: Coordinates): Number {
	return kotlin.math.sqrt(
		(
				(x - coordinates.x) * (x - coordinates.x) +
						(y - coordinates.y) * (y - coordinates.y) +
						(z - coordinates.z) * (z - coordinates.z)
				).toDouble()
	)
}

fun Coordinates.distance(coordinatesX: CoordinatesX) = distance(coordinatesX.toCoordinates())