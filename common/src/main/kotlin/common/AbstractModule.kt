package online.fivem.common.common

import kotlinx.coroutines.Job

abstract class AbstractModule {
	open fun start(): Job? {
		return null
	}

	open fun stop() {}
}