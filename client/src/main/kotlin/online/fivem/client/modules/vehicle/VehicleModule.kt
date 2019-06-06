package online.fivem.client.modules.vehicle

import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.basics.APIModule
import online.fivem.client.modules.basics.TickExecutorModule

class VehicleModule(
	private val apiModule: APIModule,
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	override suspend fun onInit() {
		moduleLoader.apply {
			add(InternetRadioModule())
			add(SpeedometerModule())
			add(BlackOutModule(apiModule))
			add(RealisticFailureModule(tickExecutorModule))
		}
	}
}