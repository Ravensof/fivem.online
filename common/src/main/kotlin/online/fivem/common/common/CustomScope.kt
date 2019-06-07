package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

object CustomScope : CoroutineScope {
	override val coroutineContext: CoroutineContext = createSupervisorJob()

}