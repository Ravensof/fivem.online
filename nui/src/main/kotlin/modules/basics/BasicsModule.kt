package online.fivem.nui.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import online.fivem.common.common.AbstractModule
import kotlin.coroutines.CoroutineContext

class BasicsModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = SupervisorJob()

	override fun init() {
		moduleLoader.add(GUIModule(coroutineContext))
	}
}