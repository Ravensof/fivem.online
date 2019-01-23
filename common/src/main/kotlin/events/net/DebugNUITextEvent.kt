package online.fivem.common.events.net

import kotlin.random.Random

class DebugNUITextEvent(
	val text: String,
	val id: Int = Random.nextInt()
)