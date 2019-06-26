package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class DebugNUITextEvent(
	val text: String,
	val id: Int
) : Serializable()