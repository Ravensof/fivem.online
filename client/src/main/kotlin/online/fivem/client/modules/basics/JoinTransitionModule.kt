package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Client
import online.fivem.common.common.BufferedAction
import online.fivem.common.common.Stack

class JoinTransitionModule(
	private val bufferedActionsModule: BufferedActionsModule,
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	private var muteHandle = Stack.UNDEFINED_INDEX

	private val bufferedAction = BufferedAction()

	init {
		Client.setManualShutdownLoadingScreenNui(true)
	}

	override fun onStart() = launch {
		bufferedActionsModule.waitForStart()

		startTransition(this@JoinTransitionModule)

		Client.doScreenFadeIn(1)
		Client.shutdownLoadingScreen()
		Client.shutdownLoadingScreenNui()

		tickExecutorModule.waitForStart()

		this@JoinTransitionModule.launch {
			delay(5_000)
			endTransition(this@JoinTransitionModule)
		}
	}

	override fun onStop(): Job? = launch {
		startTransition(this@JoinTransitionModule)
	}

	suspend fun startTransition(key: Any) = bufferedAction.start(key) {

		bufferedActionsModule.hideNui(this@JoinTransitionModule)

		bufferedActionsModule.unMuteSound(muteHandle)

		muteHandle = bufferedActionsModule.muteSound()

		if (!Client.isPlayerSwitchInProgress()) {
			Client.switchOutPlayer(Client.getPlayerPedId())
		}
	}

	suspend fun endTransition(key: Any) = bufferedAction.cancel(key) {

		val execId = tickExecutorModule.add { clearScreen() }

		bufferedActionsModule.unMuteSound(muteHandle)
		Client.switchInPlayer(Client.getPlayerPedId())
		tickExecutorModule.remove(execId)
		bufferedActionsModule.cancelHideNui(this@JoinTransitionModule)
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