package online.fivem.client.modules.eventGenerator

import kotlinx.coroutines.Job
import online.fivem.client.gtav.Natives.setTick
import online.fivem.common.common.AbstractModule

class TickExecutorModule : AbstractModule() {

	private var index = 0
	private val tickFunctions = mutableMapOf<Int, () -> Unit>()

	override fun init() {
		setTick { tickFunctions.forEach { it.value() } }
	}

	override fun stop(): Job? {
		tickFunctions.clear()

		return super.stop()
	}

	fun addTick(function: () -> Unit): Int {
		tickFunctions[++index] = function
		return index
	}

	fun removeTick(index: Int) {
		tickFunctions.remove(index)
	}
}