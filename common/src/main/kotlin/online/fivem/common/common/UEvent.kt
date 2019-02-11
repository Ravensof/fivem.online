package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

open class UEvent(override val coroutineContext: CoroutineContext) : CoroutineScope {

	open val printType = "local"

	val handlers = mutableListOf<Pair<KClass<*>, (Any) -> Unit>>()

	open fun emit(data: Any) {
		handlers.forEach {
			if (it.first.isInstance(data)) {
				it.second(data)
			}
		}
	}

	inline fun <reified T : Any> on(noinline function: (T) -> Unit) {

		handlers.add(T::class to function.unsafeCast<(Any) -> Unit>())

		Console.info("$printType event ${T::class} registered")
	}

	inline fun <reified T : Any> once(noinline function: (T) -> Unit) {
		var handler: (T) -> Unit = {}

		handler = { data ->
			function(data)
			unSubscribe(handler)
		}

		handlers.add(T::class to handler.unsafeCast<(Any) -> Unit>())

		Console.info("$printType event ${T::class} registered")
	}

	inline fun <reified T : Any> unSubscribe(noinline function: (T) -> Unit) {
		handlers.forEach {
			if (it.second == function) {
				handlers.remove(it)
				Console.info("$printType event ${T::class} unsubscribed")
			}
		}
	}

	companion object : UEvent(Job())
}
