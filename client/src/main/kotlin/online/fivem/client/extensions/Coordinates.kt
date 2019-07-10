package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.entities.Coordinates

fun Coordinates.distance(coordinates: Coordinates): Float {
	return Natives.getDistanceBetweenCoords(this, coordinates)
}