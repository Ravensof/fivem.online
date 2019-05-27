package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.gtav.Natives.setTick
import online.fivem.common.common.Handle
import online.fivem.common.common.Stack

class TickExecutorModule : AbstractClientModule() {

	private val tickFunctions = Stack<() -> Unit>()

	override suspend fun onInit() {
		setTick {
			try {
				tickFunctions.forEach { it() }
			} catch (e: Throwable) {
				println("onTick: ${e.message}")
				throw e
			}
		}
	}

	override fun onStop(): Job? {
		tickFunctions.clear()

		return super.onStop()
	}

	fun add(function: () -> Unit): Handle {
		return tickFunctions.add(function)
	}

	fun remove(index: Handle) {
		tickFunctions.remove(index)
	}
}