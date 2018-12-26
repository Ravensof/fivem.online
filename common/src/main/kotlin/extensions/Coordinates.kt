package online.fivem.common.extensions

import online.fivem.common.entities.Coordinates

fun Coordinates.toArray(): Array<Float> {
	return arrayOf(x, y, z)
}