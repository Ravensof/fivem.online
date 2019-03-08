package online.fivem.common.common

import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

typealias EntityId = Int
typealias Handle = Int

fun createJob(parent: Job? = null): CoroutineContext {
	return Job(parent) + ExceptionsStorage.coroutineExceptionHandler
}

fun createSupervisorJob(parent: Job? = null): CoroutineContext {
	return SupervisorJob(parent) + ExceptionsStorage.coroutineExceptionHandler
}