package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.gtav.NativeAudioScenes
import kotlin.coroutines.CoroutineContext

class JoinTransitionModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private var switchingPlayerJob: Job? = null
	private val tickExecutor by moduleLoader.onReady<TickExecutorModule>()

	override fun init() {
		Client.setManualShutdownLoadingScreenNui(true)
		startTransition()

		launch {
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

	fun startTransition(): Job {
		muteSound(MUTE_SOUND)

		return launch {
			if (!Client.isPlayerSwitchInProgress()) {
				switchingPlayerJob = Client.switchOutPlayer(Client.getPlayerPed())
				switchingPlayerJob?.join()
			}
		}
	}

	fun endTransition(): Job {
		return launch {
			val execId = tickExecutor.add { clearScreen() }

			switchingPlayerJob?.join()
			muteSound(false)

			Client.switchInPlayer(Client.getPlayerPed()).join()
			tickExecutor.remove(execId)
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