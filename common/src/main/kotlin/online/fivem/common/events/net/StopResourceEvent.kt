package online.fivem.common.events.net

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class StopResourceEvent(
	val eventId: Int = -1
) : Serializable()