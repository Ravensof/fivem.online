package online.fivem.common.common

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ModuleLoader {
	private var finally: (() -> Unit)? = null
	private val modules = mutableListOf<AbstractModule>()

	fun add(module: AbstractModule) {
		modules.add(module)
	}

	fun finally(function: () -> Unit) {
		finally = function
	}

	fun start() {

		GlobalScope.launch {
			modules.forEach {
				Console.log("start module ${it::class.simpleName}")
				it.start()?.join()
			}
			finally?.invoke()
		}
	}
}