package online.fivem.common.events.nui

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class DebugNUITextEvent(
	val text: String,
	val id: Int = Random.nextInt()
)