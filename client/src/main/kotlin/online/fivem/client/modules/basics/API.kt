package online.fivem.client.modules.basics

import kotlinx.coroutines.*
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.extensions.start
import online.fivem.client.extensions.stop
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
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

class API : AbstractClientModule() {

	private val tickExecutor by moduleLoader.delegate<TickExecutorModule>()
	private val controlHandlerModule by moduleLoader.delegate<ControlHandlerModule>()

	private val enableBlackOut = UnitStack()

	fun setBlackOut() = enableBlackOut.set {
		@Suppress("DEPRECATION")
		Client.setBlackout(true)
	}

	fun unSetBlackOut(handle: Handle) = enableBlackOut.unset(handle) {
		@Suppress("DEPRECATION")
		Client.setBlackout(false)
	}

	private val hideNui = UnitStack()

	fun hideNui() = hideNui.set {
		NuiEvent.emitAsync(ShowGuiEvent(false))
	}

	fun cancelHideNui(handle: Handle) = hideNui.unset(handle) {
		NuiEvent.emitAsync(ShowGuiEvent(true))
	}

	private val nuiBlackOutScreenStack = UnitStack()

	fun setBlackScreen(duration: Int = 0) = setJob(nuiBlackOutScreenStack) {
		var supportId = -1

		if (duration == 0) {
			supportId = tickExecutor.add {
				Client.drawRect(
					0.0, 0.0,
					1000.0, 2000.0,//todo подставлять разрешение экрана
					0, 0, 0
				)
			}
		}

		NuiEvent.emit(BlackOutEvent(duration))
		delay(duration.toLong())
		tickExecutor.remove(supportId)
	}

	fun unSetBlackScreen(handle: Handle, duration: Int) = unsetJob(nuiBlackOutScreenStack, handle) {
		NuiEvent.emit(CancelBlackOutEvent(duration))
		delay(duration.toLong())
	}

	fun doScreenFadeOutAsync(duration: Int) = setJob(fadeScreenStack) {
		@Suppress("DEPRECATION")
		Client.doScreenFadeOut(duration)
	}

	private val fadeScreenStack = UnitStack()

	fun doScreenFadeInJob(handle: Handle, duration: Int) = unsetJob(fadeScreenStack, handle) {
		@Suppress("DEPRECATION")
		Client.doScreenFadeIn(duration)
	}

	private val ragdollStack = UnitStack()
	private var ragdollExecutorId = Stack.UNDEFINED_INDEX


	fun setRagdollEffect(): Handle = ragdollStack.set {
		val playerPed = player.ped.entity
		ragdollExecutorId = tickExecutor.add { Client.setPedToRagdoll(playerPed) }
	}

	fun removeRagdollEffect(handle: Handle) = ragdollStack.unset(handle) {
		tickExecutor.remove(ragdollExecutorId)
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