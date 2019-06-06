package online.fivem.common.events.net

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class ErrorReportEvent(
	val message: String
) : Serializable()