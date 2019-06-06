package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class ShowGuiEvent(
	val show: Boolean = true
) : Serializable()