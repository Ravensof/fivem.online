package online.fivem.common.common

import kotlinx.coroutines.Job

abstract class AbstractModule {
	lateinit var moduleLoader: ModuleLoader

	open fun onInit() {}

	open fun onStart(): Job? = null

	open fun onStop(): Job? = null
}