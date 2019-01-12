package online.fivem.client.modules.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.client.modules.eventGenerator.TickExecutor
import online.fivem.common.common.AbstractModule
import online.fivem.common.gtav.NativeAudioScenes

class JoinTransitionModule : AbstractModule() {

	private var switchingPlayerJob: Job? = null

	override fun init() {
		Client.setManualShutdownLoadingScreenNui(true)
		startTransition()

		GlobalScope.launch {
			switchingPlayerJob?.join()

			Client.shutdownLoadingScreen()
			Client.doScreenFadeOut(0)
			Client.shutdownLoadingScreenNui()
			Client.doScreenFadeIn(500).join()
		}
	}

	override fun start(): Job? {
		return super.start()
	}

	fun startTransition() {
		muteSound(MUTE_SOUND)

		if (!Client.isPlayerSwitchInProgress()) {
			switchingPlayerJob = Client.switchOutPlayer(Client.getPlayerPed())
		}
	}

	fun endTransition(): Job {
		return GlobalScope.launch {
			val execId = TickExecutor.addTick { clearScreen() }

			switchingPlayerJob?.join()

			muteSound(false)

			Client.switchInPlayer(Client.getPlayerPed()).join()

			TickExecutor.removeTick(execId)
			Client.clearDrawOrigin()
		}
	}

	private fun clearScreen() {
		Client.setCloudHatOpacity(CLOUD_OPACITY)
		Client.hideHudAndRadarThisFrame()
		Client.setDrawOrigin(0, 0, 0, 0)
	}

	private fun muteSound(mute: Boolean) {
		if (mute) {
			Client.startAudioScene(NativeAudioScenes.MP_LEADERBOARD_SCENE.name)
		} else {
			Client.stopAudioScene(NativeAudioScenes.MP_LEADERBOARD_SCENE.name)
		}
	}

	companion object {
		const val CLOUD_OPACITY = 0.01
		const val MUTE_SOUND = true
	}
}