package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

class PlaySoundEvent(
	val sound: String,
	val volume: Double = 1.0
) : Serializable()