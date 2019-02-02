package online.fivem.common.events.net

import kotlinx.serialization.Serializable

@Serializable
class RequestPackEvent(
	val kClasses: List<String>
)