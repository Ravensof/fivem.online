package online.fivem.common.events.nui

import online.fivem.common.other.Serializable
import kotlin.random.Random

@kotlinx.serialization.Serializable
class DebugNUITextEvent(
	val text: String,
	val id: Int = Random.nextInt()
) : Serializable()