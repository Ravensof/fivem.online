package online.fivem.client.modules.basics

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.extensions.start
import online.fivem.client.extensions.stop
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.BufferedAction
import online.fivem.common.common.Locker
import online.fivem.common.common.generateLong
import online.fivem.common.events.nui.BlackOutEvent
import online.fivem.common.events.nui.CancelBlackOutEvent
import online.fivem.common.events.nui.ShowGuiEvent
import online.fivem.common.gtav.NativeAudioScenes
import online.fivem.common.gtav.NativeControls

class BufferedActionsModule(
	private val tickExecutorModule: TickExecutorModule,
	private val controlHandlerModule: ControlHandlerModule
) : AbstractClientModule() {

	val coordinatesLocker = Locker()

	private val ragdollExecutorId = generateLong()

//	private val enableBlackOut = BufferedAction()
//	private val fadeScreenStack = BufferedAction()

	private val nuiBlackOutScreenStack = BufferedAction()
	private val ragdollBuffer = BufferedAction()
	private val controlEffectsBuffer = BufferedAction()
	private val hideNuiBuffer = BufferedAction()
	private val soundEffectsBuffer = BufferedAction()

	override fun onStart() = launch {
		tickExecutorModule.waitForStart()
		controlHandlerModule.waitForStart()
	}


//	suspend fun setBlackOut(key: Any) = enableBlackOut.start(key) {
//		@Suppress("DEPRECATION")
//		Client.setBlackout(true)
//	}
//
//	suspend fun unSetBlackOut(key: Any) = enableBlackOut.cancel(key) {
//		@Suppress("DEPRECATION")
//		Client.setBlackout(false)
//	}


	suspend fun hideNui(key: Any) = hideNuiBuffer.start(key) {
		NuiEvent.emit(ShowGuiEvent(false))
	}

	suspend fun cancelHideNui(key: Any) = hideNuiBuffer.cancel(key) {
		NuiEvent.emit(ShowGuiEvent(true))
	}


	suspend fun setBlackScreen(key: Any, transitionTime: Int = 0) = nuiBlackOutScreenStack.start(key) {
		val supportId = generateLong()

		if (transitionTime == 0) {
			tickExecutorModule.add(supportId) {
				Client.drawRect(
					0.0, 0.0,
					1000.0, 2000.0,//todo подставлять разрешение экрана
					0, 0, 0
				)
			}
		}

		NuiEvent.emit(BlackOutEvent(transitionTime))
		delay(transitionTime.toLong())
		tickExecutorModule.remove(supportId)
	}

	suspend fun unSetBlackScreen(key: Any, transitionDuration: Int) = nuiBlackOutScreenStack.cancel(key) {
		NuiEvent.emit(CancelBlackOutEvent(transitionDuration))
		delay(transitionDuration.toLong())
	}


//	suspend fun doScreenFadeOutAsync(key:Any,duration: Int) = fadeScreenStack.start(key) {
//		@Suppress("DEPRECATION")
//		Client.doScreenFadeOut(duration)
//	}
//
//	suspend fun doScreenFadeInJob(key: Any, transitionDuration: Int) = fadeScreenStack.cancel(key) {
//		@Suppress("DEPRECATION")
//		Client.doScreenFadeIn(transitionDuration)
//	}

	suspend fun setRagdollEffect(key: Any) = ragdollBuffer.start(key) {
		val playerPed = player.ped.entity
		tickExecutorModule.add(ragdollExecutorId) { Client.setPedToRagdoll(playerPed) }
	}

	suspend fun removeRagdollEffect(key: Any) = ragdollBuffer.cancel(key) {
		tickExecutorModule.remove(ragdollExecutorId)
	}


	suspend fun lockControl(key: Any) = controlEffectsBuffer.start(key) {
		controlHandlerModule.addListener(LockControlListener)
	}

	suspend fun unLockControl(key: Any) = controlEffectsBuffer.cancel(key) {
		controlHandlerModule.removeListener(LockControlListener)
	}


	suspend fun muteSound(key: Any) = soundEffectsBuffer.start(key) {
		NativeAudioScenes.MP_LEADERBOARD_SCENE.start()
	}

	suspend fun unMuteSound(key: Any) = soundEffectsBuffer.cancel(key) {
		NativeAudioScenes.MP_LEADERBOARD_SCENE.stop()
	}


	private object LockControlListener : ControlHandlerModule.Listener {
		override val registeredKeys: List<NativeControls.Keys> = NativeControls.Keys.values().toList()
		override fun onJustPressed(control: NativeControls.Keys): Boolean = true
		override fun onJustReleased(control: NativeControls.Keys): Boolean = true
		override fun onLongPressed(control: NativeControls.Keys): Boolean = true
		override fun onShortPressed(control: NativeControls.Keys): Boolean = true
	}
}