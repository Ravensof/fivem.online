package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class CancelBlackOutEvent(
	val duration: Int
) : Serializable()