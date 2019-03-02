package online.fivem.common.events.net

import kotlinx.serialization.Serializable

@Serializable
class ErrorReportEvent(
	val message: String
)