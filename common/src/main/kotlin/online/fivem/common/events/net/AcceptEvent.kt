package online.fivem.common.events.net

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class AcceptEvent(
	val eventId: Int
) : Serializable()