package online.fivem.client.modules.eventGenerator

import online.fivem.client.gtav.Natives.setTick

object TickExecutor {

	private var index = 0
	private val tickFunctions = mutableMapOf<Int, () -> Unit>()

	init {
		setTick { tickFunctions.forEach { it.value() } }
	}

	fun addTick(function: () -> Unit): Int {
		tickFunctions[++index] = function
		return index
	}

	fun removeTick(index: Int) {
		tickFunctions.remove(index)
	}
}