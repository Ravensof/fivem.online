package online.fivem.server.modules.roleplay_system

import kotlinx.coroutines.CoroutineScope
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.createJob
import kotlin.coroutines.CoroutineContext

class RolePlaySystemModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = createJob()

	override fun onInit() {

	}
}