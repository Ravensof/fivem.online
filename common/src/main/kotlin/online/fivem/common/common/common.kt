package online.fivem.common.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

typealias EntityId = Int
typealias Handle = Int

var defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

fun createSupervisorJob(parent: Job? = null): CoroutineContext {
	return defaultDispatcher + SupervisorJob(parent) + ExceptionsStorage.coroutineExceptionHandler
}

private var lastLong = Long.MIN_VALUE

fun generateLong() = ++lastLong