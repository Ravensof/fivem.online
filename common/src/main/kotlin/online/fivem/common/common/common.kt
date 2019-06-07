package online.fivem.common.common

import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

typealias EntityId = Int
typealias Handle = Int

fun createSupervisorJob(parent: Job? = null): CoroutineContext {
	return SupervisorJob(parent) + ExceptionsStorage.coroutineExceptionHandler
}

private var lastLong = Long.MIN_VALUE

fun generateLong() = ++lastLong