package online.fivem.common.common

import kotlinx.coroutines.Job

abstract class AbstractModule {
	lateinit var moduleLoader: ModuleLoader.Companion

	open fun start(): Job? {
		return null
	}

	open fun stop() {}
}