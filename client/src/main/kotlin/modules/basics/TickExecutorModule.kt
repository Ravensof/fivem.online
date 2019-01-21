package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.gtav.Natives.setTick
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Stack

class TickExecutorModule : AbstractModule() {

	private val tickFunctions = Stack<() -> Unit>()

	override fun init() {
		setTick { tickFunctions.forEach { it() } }
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