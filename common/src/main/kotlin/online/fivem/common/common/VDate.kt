package online.fivem.common.common

import kotlin.js.Date

class VDate(serverTime: Double = Date.now()) {

	private var timeSpeed = 1.0
	private var timeZone = 7

	private val additionalTime = serverTime - Date.now() + timeZone * HOUR
	private val date get() = Date((Date.now() + additionalTime) * timeSpeed)

	val year get() = date.getUTCFullYear()
	val month get() = date.getUTCMonth()
	val day get() = date.getUTCDate()

	val hour get() = date.getUTCHours()
	val minute get() = date.getUTCMinutes()
	val second get() = date.getUTCSeconds()

	companion object {
		const val HOUR = 3_600_000
	}
}