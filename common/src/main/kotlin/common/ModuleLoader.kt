package online.fivem.common.common

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.events.ModuleLoadedEvent

class ModuleLoader {
	private val queue = Channel<AbstractModule>(128)
	private var finally: (() -> Unit)? = null

	private val modules = mutableListOf<AbstractModule>()

	fun add(module: AbstractModule) {
		module.moduleLoader = this@ModuleLoader
		module.init()
		GlobalScope.launch {
			queue.send(module)
		}
	}

	fun finally(function: () -> Unit) {
		finally = function
	}

	fun start() {
		GlobalScope.launch {

			while (!queue.isEmpty) {
				val module = queue.receive()
				Console.log("start module ${module::class.simpleName}")
				module.start()?.join()
				UEvent.emit(ModuleLoadedEvent(module))
				modules.add(module)
			}
			finally?.invoke()
		}
	}

	fun stop() {
		GlobalScope.launch {
			modules.asReversed().forEach {
				Console.log("stop module ${it::class.simpleName}")
				it.stop()?.join()
			}
		}
	}

	inline fun <reified T : AbstractModule> on(noinline function: (T) -> Unit) {
		UEvent.on<ModuleLoadedEvent> {
			if (it.module is T) {
				function(it.module)
			}
		}
	}
}