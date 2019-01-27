package online.fivem.nui.modules.vehicle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import kotlin.coroutines.CoroutineContext

class VehicleModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	override fun onInit() {
		moduleLoader.apply {
			add(InternetRadio())
			add(Speedometer(coroutineContext))
		}
	}
}