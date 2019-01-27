package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.gtav.Natives.setTick
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Handle
import online.fivem.common.common.Stack

class TickExecutorModule : AbstractModule() {

	private val tickFunctions = Stack<() -> Unit>()

	override fun onInit() {
		setTick { tickFunctions.forEach { it() } }
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