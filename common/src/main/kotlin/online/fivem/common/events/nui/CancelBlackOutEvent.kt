package online.fivem.common.events.nui

import kotlinx.serialization.Serializable

@Serializable
class CancelBlackOutEvent(
	val duration: Int
)