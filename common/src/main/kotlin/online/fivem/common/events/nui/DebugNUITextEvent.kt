package online.fivem.common.events.nui

import online.fivem.common.other.Serializable
import kotlin.random.Random

class DebugNUITextEvent(
	val text: String,
	val id: Int = Random.nextInt()
) : Serializable()