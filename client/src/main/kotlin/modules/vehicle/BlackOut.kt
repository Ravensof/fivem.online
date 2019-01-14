package online.fivem.client.modules.vehicle

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.events.AccelerationThresholdAchievedEvent

class BlackOut : AbstractModule() {

	private var isBlackedOut = false

	override fun init() {

		UEvent.on<AccelerationThresholdAchievedEvent> { blackOut(GlobalConfig.BlackOut.blackOutTime) }
	}

	private fun blackOut(timeMillis: Long): Job {
		return GlobalScope.launch {
			if (isBlackedOut) return@launch

			Client.doScreenFadeOut(100).join()

			delay(timeMillis)

			Client.doScreenFadeIn(timeMillis.toInt() / 10)
		}
	}
}