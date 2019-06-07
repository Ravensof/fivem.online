package online.fivem.common.events.net

import online.fivem.common.entities.CoordinatesX
import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class SpawnPlayerEvent(
	val coordinatesX: CoordinatesX,
	val pedModel: Int? = null,
	val weapons: Map<String, Int> = mapOf(),
	val health: Int = 200,
	val armour: Int = 0
) : Serializable()