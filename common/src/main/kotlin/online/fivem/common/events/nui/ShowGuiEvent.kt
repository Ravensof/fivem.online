package online.fivem.common.events.nui

import kotlinx.serialization.Serializable

@Serializable
class ShowGuiEvent(
	val show: Boolean = true
)