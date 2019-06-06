package online.fivem.common.common

import online.fivem.common.GlobalConfig
import kotlin.js.Date

class VDate(currentVirtualTime: Double? = null) {

	private var swipeTime: Double = 0.0

	var timeSpeed: Double = GlobalConfig.GAME_TIME_SPEED
	var timeZone: Int = GlobalConfig.SERVER_TIMEZONE

	var serverRealTime: Double = Date.now()

	private val additionalTime = serverRealTime - Date.now()

	val time get() = (Date.now() + additionalTime) * timeSpeed + swipeTime

	init {
		currentVirtualTime?.let {
			this.swipeTime = currentVirtualTime - time
		}
	}

	private val date get() = Date(time + timeZone * HOUR)

	val year get() = date.getUTCFullYear()
	val month get() = date.getUTCMonth()
	val day get() = date.getUTCDate()

	val hour get() = date.getUTCHours()
	val minute get() = date.getUTCMinutes()
	val second get() = date.getUTCSeconds()

	val dayOfWeek get() = date.getUTCDay()


	companion object {
		const val HOUR = 3_600_000
	}
}