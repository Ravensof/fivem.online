package online.fivem.client.modules.basics

import kotlinx.coroutines.*
import online.fivem.client.extensions.startAudioScene
import online.fivem.client.extensions.stopAudioScene
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Handle
import online.fivem.common.common.Stack
import online.fivem.common.events.net.BlackOutEvent
import online.fivem.common.events.net.CancelBlackOutEvent
import online.fivem.common.gtav.NativeAudioScenes
import kotlin.coroutines.CoroutineContext

private typealias UnitStack = Stack<Unit>

class API(
	override val coroutineContext: CoroutineContext
) : AbstractModule(), CoroutineScope {

	private val tickExecutor by moduleLoader.onReady<TickExecutorModule>()

	private val nuiBlackOutScreenStack = UnitStack()

	fun doNuiBlackOut(duration: Int) = setJob(nuiBlackOutScreenStack) {
		NuiEvent.emit(BlackOutEvent(duration))
		delay(duration.toLong())
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


	fun setRagdollEffect(): Handle = set(ragdollStack) {
		val playerPed = Client.getPlayerPed()
		ragdollExecutorId = tickExecutor.add { Client.setPedToRagdoll(playerPed) }
	}

	fun removeRagdollEffect(handle: Handle) = unset(ragdollStack, handle) {
		tickExecutor.remove(ragdollExecutorId)
	}

	private val playerControlStack = UnitStack()

	fun lockControl(): Handle = set(playerControlStack) {
		@Suppress("DEPRECATION")
		Client.setPlayerControl(Client.getPlayerId(), false)
	}

	fun unLockControl(handle: Handle) = unset(playerControlStack, handle) {
		@Suppress("DEPRECATION")
		Client.setPlayerControl(Client.getPlayerId(), true)
	}

	private val muteSoundStack = UnitStack()

	fun muteSound(): Handle = set(muteSoundStack) {
		Client.startAudioScene(NativeAudioScenes.MP_LEADERBOARD_SCENE)
	}

	fun unMuteSound(handle: Handle) = unset(muteSoundStack, handle) {
		Client.stopAudioScene(NativeAudioScenes.MP_LEADERBOARD_SCENE)
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

	private fun set(stack: UnitStack, doOnce: () -> Unit): Handle {
		if (stack.isEmpty()) {
			doOnce()
		}
		return stack.add(Unit)
	}

	private fun unset(stack: UnitStack, handle: Handle, doOnce: () -> Unit) {
		stack.remove(handle)
		if (stack.isEmpty()) {
			doOnce()
		}
	}
}