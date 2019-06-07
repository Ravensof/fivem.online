package online.fivem.client.modules.basics

import kotlinx.coroutines.*
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.extensions.start
import online.fivem.client.extensions.stop
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.BufferedAction
import online.fivem.common.common.Handle
import online.fivem.common.common.Stack
import online.fivem.common.events.nui.BlackOutEvent
import online.fivem.common.events.nui.CancelBlackOutEvent
import online.fivem.common.events.nui.ShowGuiEvent
import online.fivem.common.extensions.UnitStack
import online.fivem.common.extensions.set
import online.fivem.common.extensions.unset
import online.fivem.common.gtav.NativeAudioScenes
import online.fivem.common.gtav.NativeControls

class BufferedActionsModule(
	private val tickExecutorModule: TickExecutorModule,
	private val controlHandlerModule: ControlHandlerModule
) : AbstractClientModule() {

	private val enableBlackOut = UnitStack()

	override fun onStart() = launch {
		tickExecutorModule.waitForStart()
		controlHandlerModule.waitForStart()
	}

	fun setBlackOut() = enableBlackOut.set {
		@Suppress("DEPRECATION")
		Client.setBlackout(true)
	}

	fun unSetBlackOut(handle: Handle) = enableBlackOut.unset(handle) {
		@Suppress("DEPRECATION")
		Client.setBlackout(false)
	}

	private val hideNui = BufferedAction()

	suspend fun hideNui(key: Any) = hideNui.start(key) {
		NuiEvent.emit(ShowGuiEvent(false))
	}

	suspend fun cancelHideNui(key: Any) = hideNui.cancel(key) {
		NuiEvent.emit(ShowGuiEvent(true))
	}

	private val nuiBlackOutScreenStack = UnitStack()

	fun setBlackScreen(transitionTime: Int = 0) = setJob(nuiBlackOutScreenStack) {
		var supportId = -1

		if (transitionTime == 0) {
			supportId = tickExecutorModule.add {
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

	fun unSetBlackScreen(handle: Handle, transitionDuration: Int) = unsetJob(nuiBlackOutScreenStack, handle) {
		NuiEvent.emit(CancelBlackOutEvent(transitionDuration))
		delay(transitionDuration.toLong())
	}

	fun doScreenFadeOutAsync(duration: Int) = setJob(fadeScreenStack) {
		@Suppress("DEPRECATION")
		Client.doScreenFadeOut(duration)
	}

	private val fadeScreenStack = UnitStack()

	fun doScreenFadeInJob(handle: Handle, transitionDuration: Int) = unsetJob(fadeScreenStack, handle) {
		@Suppress("DEPRECATION")
		Client.doScreenFadeIn(transitionDuration)
	}

	private val ragdollStack = UnitStack()
	private var ragdollExecutorId = Stack.UNDEFINED_INDEX


	fun setRagdollEffect(): Handle = ragdollStack.set {
		val playerPed = player.ped.entity
		ragdollExecutorId = tickExecutorModule.add { Client.setPedToRagdoll(playerPed) }
	}

	fun removeRagdollEffect(handle: Handle) = ragdollStack.unset(handle) {
		tickExecutorModule.remove(ragdollExecutorId)
	}

	private val playerControlStack = UnitStack()

	fun lockControl(): Handle = playerControlStack.set {
		controlHandlerModule.addListener(LockControlListener)
	}

	fun unLockControl(handle: Handle) = playerControlStack.unset(handle) {
		controlHandlerModule.removeListener(LockControlListener)
	}

	private val muteSoundStack = UnitStack()

	fun muteSound(): Handle = muteSoundStack.set {
		NativeAudioScenes.MP_LEADERBOARD_SCENE.start()
	}

	fun unMuteSound(handle: Handle) = muteSoundStack.unset(handle) {
		NativeAudioScenes.MP_LEADERBOARD_SCENE.stop()
	}

	private fun setJob(stack: UnitStack, doOnce: suspend () -> Unit): Deferred<Handle> = async {
		if (stack.isEmpty()) {
			doOnce()
		}
		return@async stack.add(Unit)
	}

	private fun unsetJob(stack: UnitStack, handle: Handle, doOnce: suspend () -> Unit): Job {
		stack.remove(handle)
		return launch {
			if (stack.isEmpty()) {
				doOnce()
			}
		}
	}

	private object LockControlListener : ControlHandlerModule.Listener {
		override val registeredKeys: List<NativeControls.Keys> = NativeControls.Keys.values().toList()
		override fun onJustPressed(control: NativeControls.Keys): Boolean = true
		override fun onJustReleased(control: NativeControls.Keys): Boolean = true
		override fun onLongPressed(control: NativeControls.Keys): Boolean = true
		override fun onShortPressed(control: NativeControls.Keys): Boolean = true
	}
}