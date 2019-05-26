package online.fivem.common.events.nui

import online.fivem.common.other.Serializable
import kotlin.random.Random

class PlaySoundEvent(
	val sound: String,
	val volume: Double = 1.0,
	val id: Int = Random.nextInt()
) : Serializable()