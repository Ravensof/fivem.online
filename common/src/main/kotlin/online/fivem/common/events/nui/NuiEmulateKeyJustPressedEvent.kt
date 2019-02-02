package online.fivem.common.events.nui

import kotlinx.serialization.Serializable

@Serializable
class NuiEmulateKeyJustPressedEvent(
	val code: Int,
	val durability: Long = 0
)