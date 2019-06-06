package online.fivem.common.events.nui

import online.fivem.common.other.Serializable
import kotlin.random.Random

@kotlinx.serialization.Serializable
class PlaySoundEvent(
	val sound: String,
	val volume: Double = 1.0,
	val id: Int = Random.nextInt()
) : Serializable()