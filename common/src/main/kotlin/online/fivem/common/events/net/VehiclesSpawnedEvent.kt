package online.fivem.common.events.net

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class VehiclesSpawnedEvent(
	val ids: Map<Int, Int>
) : Serializable()