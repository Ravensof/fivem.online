package online.fivem.client.modules.eventGenerator

import online.fivem.client.gtav.Natives.setTick

object TickExecutor {

	private val tickFunctions = mutableListOf<() -> Unit>()

	init {
		setTick { tickFunctions.forEach { it() } }
	}

	fun addTick(function: () -> Unit): Int {
		tickFunctions.add(function)
		return tickFunctions.lastIndex
	}

	fun removeTick(index: Int) {
		tickFunctions.removeAt(index)
	}
}