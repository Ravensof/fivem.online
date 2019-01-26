package online.fivem.client.modules.vehicle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.modules.basics.API
import online.fivem.client.modules.basics.TickExecutorModule
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.UEvent
import online.fivem.common.events.AccelerationThresholdAchievedEvent
import online.fivem.common.events.PlayerPedHealthZeroEvent
import kotlin.coroutines.CoroutineContext

class BlackOut(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val tickExecutor by moduleLoader.onReady<TickExecutorModule>()
	private val api by moduleLoader.onReady<API>()
	private var timeLeft: Long = 0

	override fun init() {
		UEvent.on<PlayerPedHealthZeroEvent> {
			Console.debug("blackout from commas")
			blackOut(0)//GlobalConfig.BlackOut.BLACKOUT_TIME_FROM_COMMAS)
		}

		UEvent.on<AccelerationThresholdAchievedEvent> {
			Console.debug("blackout from ${it.accelerationModule.toInt()} m/s^2")//1176
			blackOut(0)
		}
	}

	private fun addBlackOut(timeMillis: Long) {
		timeLeft += timeMillis
	}

	private fun blackOut(timeMillis: Long): Job = launch {
		if (timeLeft > 0) return@launch addBlackOut(timeMillis)
		timeLeft += GlobalConfig.BlackOut.EXTRA_BLACKOUT_TIME + timeMillis

		val blackOutHandle = api.doNuiBlackOut(100).await()
//		Client.doScreenFadeOut(100).join()

		val muteHandle = api.muteSound()
		val lockHandle = api.lockControl()
		val ragdollHandle = api.setRagdollEffect()

		var time: Long

		while (timeLeft > 0) {
			time = timeLeft
			delay(time)
			timeLeft -= time
		}

		api.unMuteSound(muteHandle)
		api.undoNuiBlackOut(blackOutHandle, GlobalConfig.BlackOut.WAKING_UP_TIME).join()
		api.unLockControl(lockHandle)
		api.removeRagdollEffect(ragdollHandle)
//		Client.doScreenFadeIn(GlobalConfig.BlackOut.WAKING_UP_TIME).join()
	}
}