package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class AbstractModule : CoroutineScope {

	override val coroutineContext: CoroutineContext = createSupervisorJob()

	lateinit var moduleLoader: ModuleLoader

	open fun onInit() {}

	open fun onStart(): Job? = null

	open fun onStop(): Job? {

		coroutineContext.cancel()

		return null
	}
}