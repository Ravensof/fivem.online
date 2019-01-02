package online.fivem.common.common

import kotlinx.coroutines.Job

abstract class AbstractModule {
	lateinit var moduleLoader: ModuleLoader.Companion

	open fun start(): Job? = null

	open fun stop(): Job? = null
}