package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.gtav.Client
import online.fivem.common.common.BufferedAction
import online.fivem.common.common.Console
import online.fivem.common.common.generateLong
import online.fivem.common.extensions.onNull
import online.fivem.common.gtav.NativePeds

class JoinTransitionModule(
	private val bufferedActionsModule: BufferedActionsModule,
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	private val transitionBuffer = BufferedAction()
	private val clearScreenExecutor = generateLong()

	init {
		Client.setManualShutdownLoadingScreenNui(true)
	}

	override fun onStart() = launch {
		bufferedActionsModule.waitForStart()
		tickExecutorModule.waitForStart()

		player.setModel(NativePeds.ABIGAIL.hash)//нужно для того, чтобы сработал switchOut

		startTransition(this@JoinTransitionModule)
		transitionBuffer.cancel(this@JoinTransitionModule) {}

		Client.doScreenFadeIn(1)
		Client.shutdownLoadingScreen()
		Client.shutdownLoadingScreenNui()
	}

	override fun onStop(): Job? = launch {
		startTransition(this@JoinTransitionModule)
	}

	suspend fun startTransition(key: Any) = transitionBuffer.start(key) {
		tickExecutorModule.add(clearScreenExecutor) { clearScreen() }

		launch {
			bufferedActionsModule.hideNui(this@JoinTransitionModule)
		}

		withTimeoutOrNull(20_000) {
			player.ped.switchOut()
			true
		}.onNull {
			Console.warn("JoinTransitionModule: cannot switch out player")
		}

		launch {
			bufferedActionsModule.muteSound(this@JoinTransitionModule)
		}
	}

	suspend fun endTransition(key: Any) = transitionBuffer.cancel(key) {

		bufferedActionsModule.unMuteSound(this)
		withTimeoutOrNull(40_000) {
			player.ped.switchIn()
			true
		}.onNull {
			Console.warn("JoinTransitionModule: cannot switch in player")
		}
		tickExecutorModule.remove(clearScreenExecutor)
		bufferedActionsModule.cancelHideNui(this)
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