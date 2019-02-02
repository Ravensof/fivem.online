package online.fivem.nui.modules.vehicle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import online.fivem.common.common.AbstractModule
import kotlin.coroutines.CoroutineContext

class VehicleModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = SupervisorJob()

	override fun onInit() {
		moduleLoader.apply {
			add(InternetRadio())
			add(SpeedometerModule(coroutineContext))
		}
	}
}