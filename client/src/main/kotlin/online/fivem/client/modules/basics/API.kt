package online.fivem.client.modules.basics

import kotlinx.coroutines.*
import online.fivem.client.extensions.start
import online.fivem.client.extensions.stop
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Handle
import online.fivem.common.common.Stack
import online.fivem.common.common.UEvent
import online.fivem.common.entities.Coordinates
import online.fivem.common.events.PlayersPedTeleportedEvent
import online.fivem.common.events.PlayersPedTeleportingEvent
import online.fivem.common.events.net.BlackOutEvent
import online.fivem.common.events.net.CancelBlackOutEvent
import online.fivem.common.events.net.ShowGuiEvent
import online.fivem.common.extensions.UnitStack
import online.fivem.common.extensions.set
import online.fivem.common.extensions.unset
import online.fivem.common.gtav.NativeAudioScenes
import online.fivem.common.gtav.NativeControls
import kotlin.coroutines.CoroutineContext

class API(
	override val coroutineContext: CoroutineContext
) : AbstractModule(), CoroutineScope {

	private val tickExecutor by moduleLoader.onReady<TickExecutorModule>()
	private val controlHandlerModule by moduleLoader.onReady<ControlHandlerModule>()

	private val hideNui = UnitStack()

	fun hideNui() = hideNui.set {
		NuiEvent.emit(ShowGuiEvent(false))
	}

	fun cancelHideNui(handle: Handle) = hideNui.unset(handle) {
		NuiEvent.emit(ShowGuiEvent(true))
	}

	fun setPlayerCoordinates(coordinates: Coordinates) {
		launch {
			UEvent.emit(PlayersPedTeleportingEvent())
			delay(1_000)
			Client.setEntityCoordsNoOffset(Client.getPlayerPed(), coordinates.x, coordinates.y, coordinates.z)
			delay(1_000)
			UEvent.emit(PlayersPedTeleportedEvent())
		}
	}

	private val nuiBlackOutScreenStack = UnitStack()

	fun doNuiBlackOut(duration: Int = 0) = setJob(nuiBlackOutScreenStack) {
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

	fun undoNuiBlackOut(handle: Handle, duration: Int) = unsetJob(nuiBlackOutScreenStack, handle) {
		NuiEvent.emit(CancelBlackOutEvent(duration))
		delay(duration.toLong())
	}

	fun doScreenFadeOut(duration: Int) = setJob(fadeScreenStack) {
		@Suppress("DEPRECATION")
		Client.doScreenFadeOut(duration).join()
	}

	private val fadeScreenStack = UnitStack()

	fun doScreenFadeIn(handle: Handle, duration: Int) = unsetJob(fadeScreenStack, handle) {
		@Suppress("DEPRECATION")
		Client.doScreenFadeIn(duration).join()
	}

	private val ragdollStack = UnitStack()
	private var ragdollExecutorId = Stack.UNDEFINED_INDEX


	fun setRagdollEffect(): Handle = ragdollStack.set {
		val playerPed = Client.getPlayerPed()
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