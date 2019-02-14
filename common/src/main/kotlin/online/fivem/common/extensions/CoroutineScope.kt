package online.fivem.common.extensions

import kotlinx.coroutines.*
import kotlin.js.Date

fun CoroutineScope.repeatJob(timeMillis: Long, function: () -> Unit): Job = launch {
	var startTime: Double
	var endTime: Double

	while (isActive) {
		startTime = Date.now()
		function()

		endTime = Date.now()
		if (endTime - startTime < timeMillis) {
			delay((timeMillis - endTime + startTime).toLong())
		}
	}
}