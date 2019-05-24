package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Client
import online.fivem.common.common.Stack

class JoinTransitionModule : AbstractClientModule() {

	private var switchingPlayerJob: Job? = null
	private val tickExecutor by moduleLoader.delegate<TickExecutorModule>()

	private lateinit var api: API

	private var muteHandle = Stack.UNDEFINED_INDEX

	override fun onStart() = launch {
		api = moduleLoader.getModule(API::class)

		Client.setManualShutdownLoadingScreenNui(true)
		startTransitionJob().join()

		Client.doScreenFadeIn(1)
		Client.shutdownLoadingScreen()
		Client.shutdownLoadingScreenNui()
	}

	override fun onStop(): Job? = launch {
		startTransitionJob().join()
	}

	fun startTransitionJob(): Job {
		api.unMuteSound(muteHandle)
		muteHandle = api.muteSound()

		return launch {
			if (!Client.isPlayerSwitchInProgress()) {
				switchingPlayerJob = launch { Client.switchOutPlayer(Client.getPlayerPedId()) }
				switchingPlayerJob?.join()
			}
		}
	}

	fun endTransitionJob(): Job = launch {
		val execId = tickExecutor.add { clearScreen() }

		switchingPlayerJob?.join()
		api.unMuteSound(muteHandle)

		Client.switchInPlayer(Client.getPlayerPedId())
		tickExecutor.remove(execId)
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