package online.fivem.client.modules.eventGenerator

import kotlinx.coroutines.Job
import online.fivem.client.common.Stack
import online.fivem.client.gtav.Natives.setTick
import online.fivem.common.common.AbstractModule

class TickExecutorModule : AbstractModule() {

	private val tickFunctions = Stack<() -> Unit>()

	override fun init() {
		setTick { tickFunctions.stack.forEach { it() } }
	}

	override fun stop(): Job? {
		tickFunctions.clear()

		return super.stop()
	}

	fun add(function: () -> Unit): Int {
		return tickFunctions.add(function)
	}

	fun remove(index: Int) {
		tickFunctions.remove(index)
	}
}