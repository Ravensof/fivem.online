package online.fivem.common.common

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ModuleLoader {
	private var finally: (() -> Unit)? = null

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
				it.moduleLoader = ModuleLoader.Companion
				it.start()?.join()
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

	companion object {
		private val modules = mutableListOf<AbstractModule>()

		fun <T : AbstractModule> get(kClass: KClass<T>): T? {
			return modules.find { kClass == it::class } as T?
		}
	}
}