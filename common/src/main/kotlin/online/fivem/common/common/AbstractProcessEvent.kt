package online.fivem.common.common

import kotlinx.coroutines.Deferred

abstract class AbstractProcessEvent<T>(
	private val result: Deferred<T>,
	private val onComplete: (T) -> Unit = {}
) {
	init {
		result.invokeOnCompletion {
			val result = result.getCompleted()
			onComplete(result)
		}

		result.start()
	}

	fun start() = result.start()

	suspend fun join() = result.join()

	suspend fun await(): T = result.await()

	fun cancel() = result.cancel()
}