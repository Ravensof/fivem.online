package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.entities.Coordinates

fun Coordinates.distance(coordinates: Coordinates): Float {
	return Client.getDistanceBetweenCoords(this, coordinates)
}