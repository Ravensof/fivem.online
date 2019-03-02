package online.fivem.common.common

import online.fivem.common.extensions.onNull

object ExceptionsStorage {

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
				throw throwable
			}
	}

	interface Listener {
		fun onThrowable(throwable: Throwable)
	}
}