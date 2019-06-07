package online.fivem.client.modules.vehicle

import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.basics.BufferedActionsModule
import online.fivem.client.modules.basics.TickExecutorModule

class VehicleModule(
	private val bufferedActionsModule: BufferedActionsModule,
	private val tickExecutorModule: TickExecutorModule
) : AbstractClientModule() {

	override suspend fun onInit() {
		moduleLoader.apply {
			add(InternetRadioModule())
			add(SpeedometerModule())
			add(BlackOutModule(bufferedActionsModule))
			add(RealisticFailureModule(tickExecutorModule))
		}
	}
}