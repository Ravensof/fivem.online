package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Client
import online.fivem.common.common.Stack

//rename to effects?
class JoinTransitionModule(
	private val apiModule: APIModule,
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	private var switchingPlayerJob: Job? = null

	private var muteHandle = Stack.UNDEFINED_INDEX

	init {
		Client.setManualShutdownLoadingScreenNui(true)
	}

	override fun onStart() = launch {
		tickExecutorModule.waitForStart()
		apiModule.waitForStart()

		startTransitionJob()
		switchingPlayerJob?.join()
		Client.doScreenFadeIn(1)
		Client.shutdownLoadingScreen()
		Client.shutdownLoadingScreenNui()
	}

	override fun onStop(): Job? = launch {
		startTransitionJob().join()
	}

	fun startTransitionJob(): Job {
		apiModule.unMuteSound(muteHandle)
		muteHandle = apiModule.muteSound()

		return launch {
			if (!Client.isPlayerSwitchInProgress()) {
				switchingPlayerJob = launch { Client.switchOutPlayer(Client.getPlayerPedId()) }
				switchingPlayerJob?.join()
			}
		}
	}

	fun endTransitionJob(): Job = launch {
		val execId = tickExecutorModule.add { clearScreen() }

		switchingPlayerJob?.join()
		apiModule.unMuteSound(muteHandle)

		Client.switchInPlayer(Client.getPlayerPedId())
		tickExecutorModule.remove(execId)
		Client.clearDrawOrigin()
	}

	private fun clearScreen() {
		Client.setCloudHatOpacity(CLOUD_OPACITY)
		Client.hideHudAndRadarThisFrame()
		Client.setDrawOrigin(0, 0, 0, 0)
	}

	companion object {
		const val CLOUD_OPACITY = 0.01
	}
}