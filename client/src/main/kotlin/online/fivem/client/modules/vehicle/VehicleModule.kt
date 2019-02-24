package online.fivem.client.modules.vehicle

import kotlinx.coroutines.CoroutineScope
import online.fivem.common.common.AbstractModule
import kotlin.coroutines.CoroutineContext

class VehicleModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = createJob()

	override fun onInit() {
		moduleLoader.apply {
			add(InternetRadio(coroutineContext))
			add(Speedometer(coroutineContext))
			add(BlackOut(coroutineContext))
			add(RealisticFailureModule(coroutineContext))
		}
	}
}