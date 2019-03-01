package online.fivem.nui.modules.vehicle

import online.fivem.common.common.AbstractModule

class VehicleModule : AbstractModule() {

	override fun onInit() {
		moduleLoader.apply {
			add(InternetRadio())
			add(SpeedometerModule(coroutineContext))
		}
	}
}