package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.gtav.Client
import online.fivem.common.common.BufferedAction
import online.fivem.common.common.Console
import online.fivem.common.common.generateLong
import online.fivem.common.extensions.onNull

class JoinTransitionModule(
	private val bufferedActionsModule: BufferedActionsModule,
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	private val transitionBuffer = BufferedAction()
	private val clearScreenExecutor = generateLong()

	init {
		Client.setManualShutdownLoadingScreenNui(true)
	}

	override fun onStartAsync() = async {
		bufferedActionsModule.waitForStart()
		tickExecutorModule.waitForStart()

		withTimeoutOrNull(10_000) {
			player.ped.switchOut()//todo перед спавном игрока работает через раз
			true
		}.onNull {
			Console.warn("JoinTransitionModule: cannot switch out player")
		}

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