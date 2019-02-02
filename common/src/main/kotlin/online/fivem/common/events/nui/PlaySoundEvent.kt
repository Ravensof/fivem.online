package online.fivem.common.events.nui

import kotlinx.serialization.Serializable

@Serializable
class PlaySoundEvent(
	val sound: String,
	val volume: Double = 1.0
)