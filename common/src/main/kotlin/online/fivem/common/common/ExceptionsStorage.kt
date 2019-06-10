package online.fivem.common.common

import kotlinx.coroutines.CoroutineExceptionHandler
import online.fivem.common.extensions.onNull
import online.fivem.common.extensions.stackTrace
import kotlin.coroutines.CoroutineContext

object ExceptionsStorage {

	val coroutineExceptionHandler =
		CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
			add(throwable)
		}

	var listener: Listener? = null
		set(value) {
			field = value ?: return

			exceptions.forEach { value.onThrowable(it) }
			exceptions.clear()
		}

	private val exceptions = mutableListOf<Throwable>()

	fun add(throwable: Throwable) {
		listener
			?.onThrowable(throwable)
			.onNull {
				exceptions += throwable
				Console.error(throwable.stackTrace())
			}
	}

	interface Listener {
		fun onThrowable(throwable: Throwable)
	}
}