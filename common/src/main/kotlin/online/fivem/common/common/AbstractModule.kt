package online.fivem.common.common

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class AbstractModule {
	lateinit var moduleLoader: ModuleLoader

	open fun onInit() {}

	open fun onStart(): Job? = null

	open fun onStop(): Job? = null

	protected open fun coroutineExceptionHandler(coroutineContext: CoroutineContext, throwable: Throwable) {
		Console.debug("exception: ${throwable.message}")
	}

	protected fun CoroutineScope.createJob(parent: Job? = null): CoroutineContext {
		return Job(parent) + CoroutineExceptionHandler(::coroutineExceptionHandler)
	}

	protected fun CoroutineScope.createSupervisorJob(parent: Job? = null): CoroutineContext {
		return SupervisorJob(parent) + CoroutineExceptionHandler(::coroutineExceptionHandler)
	}
}