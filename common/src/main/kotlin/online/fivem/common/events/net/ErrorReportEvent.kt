package online.fivem.common.events.net

import online.fivem.common.other.Serializable

class ErrorReportEvent(
	val message: String
) : Serializable()