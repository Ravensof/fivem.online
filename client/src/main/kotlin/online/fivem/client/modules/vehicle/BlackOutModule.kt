package online.fivem.client.modules.vehicle

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.events.AccelerationThresholdAchievedEvent
import online.fivem.client.events.PlayerPedSpawnedEvent
import online.fivem.client.events.PlayersPedHealthChangedEvent
import online.fivem.client.extensions.play
import online.fivem.client.extensions.prefetch
import online.fivem.client.gtav.Client
import online.fivem.client.modules.basics.BufferedActionsModule
import online.fivem.common.GlobalConfig.BlackOut.ACCELERATION_THRESHOLD
import online.fivem.common.GlobalConfig.BlackOut.BLACKOUT_TIME_FROM_COMMAS
import online.fivem.common.GlobalConfig.BlackOut.EXTRA_BLACKOUT_TIME
import online.fivem.common.GlobalConfig.BlackOut.WAKING_UP_TIME
import online.fivem.common.Sounds
import online.fivem.common.common.Console
import online.fivem.common.common.Event

class BlackOutModule(
	private val bufferedActionsModule: BufferedActionsModule
) : AbstractClientModule() {

	private var timeLeft: Long = 0
	private var waiting: Job? = null
	private var enabled = false

	override suspend fun onInit() {
		launch {
			Sounds.SHOCK_EFFECT.prefetch()
		}
	}

	override fun onStart() = launch {
		bufferedActionsModule.waitForStart()

		Event.apply {
			on<PlayerPedSpawnedEvent> {
				waiting?.cancel()
				timeLeft = 0
				enabled = true
			}
			on<PlayersPedHealthChangedEvent.Zero> {
				blackOut(BLACKOUT_TIME_FROM_COMMAS * 1_000)
			}
			on<AccelerationThresholdAchievedEvent> {
				if (!enabled) return@on
				if (it.accelerationModule < ACCELERATION_THRESHOLD) return@on

				val playerPed = Client.getPlayerPedId()
				val currentHealth = Client.getEntityHealth(playerPed)

				Client.setEntityHealth(playerPed, currentHealth - it.accelerationModule.toInt() / 50)
				Console.debug("blackout from ${it.accelerationModule} m/s^2")
				blackOut(
					(
							if (timeLeft > 0)
								it.accelerationModule - ACCELERATION_THRESHOLD
							else
								it.accelerationModule
							).toLong() * 100
				)
			}
		}
	}

	override fun onStop(): Job? {
		timeLeft = 0

		return null
	}

	private fun addBlackOut(timeMillis: Long) {
		timeLeft += timeMillis
	}

	private fun blackOut(timeMillis: Long): Job = launch {
		if (timeLeft > 0) return@launch addBlackOut(timeMillis)
		timeLeft += EXTRA_BLACKOUT_TIME * 1_000 + timeMillis

		launch { bufferedActionsModule.setBlackScreen(this@BlackOutModule) }

		launch { bufferedActionsModule.muteSound(this@BlackOutModule) }
		launch { bufferedActionsModule.lockControl(this@BlackOutModule) }
		launch { bufferedActionsModule.setRagdollEffect(this@BlackOutModule) }

		try {
			waiting().join()
		} catch (e: CancellationException) {
		}

		launch { Sounds.SHOCK_EFFECT.play() }
		delay(2_000)
		bufferedActionsModule.unMuteSound(this@BlackOutModule)
		bufferedActionsModule.unLockControl(this@BlackOutModule)
		bufferedActionsModule.unSetBlackScreen(this@BlackOutModule, WAKING_UP_TIME * 1_000)
		bufferedActionsModule.removeRagdollEffect(this@BlackOutModule)
	}

	private fun waiting() = launch {
		var time: Long

		while (timeLeft > 0) {
			time = timeLeft
			delay(time)
			timeLeft -= time
		}
	}.also {
		waiting?.cancel()
		waiting = it
	}
}