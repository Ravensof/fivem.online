package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class BlackOutEvent(
	val duration: Int
) : Serializable()