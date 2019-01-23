package online.fivem.server.modules.rolePlaySystem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import online.fivem.common.common.AbstractModule
import kotlin.coroutines.CoroutineContext

class RolePlaySystemModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = SupervisorJob()

	override fun init() {
		moduleLoader.add(SynchronizationModule(coroutineContext))
	}
}