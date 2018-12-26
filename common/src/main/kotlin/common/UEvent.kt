package online.fivem.common.common

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

open class UEvent {

	open val printType = "local"

	val handlers = mutableListOf<(Any) -> Unit>()

	inline fun <reified T : Any> emit(data: T): Job {
		return emit(T::class, data)
	}

	open fun emit(kClass: KClass<out Any>, data: Any): Job {
		return GlobalScope.launch {
			handlers.forEach { it.invoke(data) }
		}
	}

	inline fun <reified T> on(noinline function: (T) -> Unit) {
		handlers.add {
			if (it is T) {
				function(it)
			}
		}
		Console.info("$printType event ${T::class} registered")
	}

	companion object : UEvent()
}
