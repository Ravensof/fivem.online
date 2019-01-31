package online.fivem.common.events.nui

import kotlin.random.Random

class DebugNUITextEvent(
	val text: String,
	val id: Int = Random.nextInt()
)