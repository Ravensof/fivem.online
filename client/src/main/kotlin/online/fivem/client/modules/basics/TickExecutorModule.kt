package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.Natives
import online.fivem.client.common.AbstractClientModule
import online.fivem.extensions.setTick

class TickExecutorModule : AbstractClientModule() {

	private val tickFunctions = mutableMapOf<Any, () -> Unit>()

	override suspend fun onInit() {
		Natives.setTick {
			try {
				tickFunctions.forEach { it.value() }
			} catch (e: Throwable) {
				ErrorReporterModule.handleError(
					Exception("TickExecutorModule::onTick: ${e.message}", e)
				)
			}
		}
	}

	override fun onStop(): Job? {
		tickFunctions.clear()

		return super.onStop()
	}

	fun add(key: Any, function: () -> Unit) {
		tickFunctions[key] = function
	}

	fun remove(key: Any) {
		tickFunctions.remove(key)
	}
}