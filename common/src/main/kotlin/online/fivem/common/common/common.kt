package online.fivem.common.common

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

typealias EntityId = Int
typealias Handle = Int

private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
	ExceptionsStorage.add(throwable)
}

fun createJob(parent: Job? = null): CoroutineContext {
	return Job(parent) + coroutineExceptionHandler
}

fun createSupervisorJob(parent: Job? = null): CoroutineContext {
	return SupervisorJob(parent) + coroutineExceptionHandler
}