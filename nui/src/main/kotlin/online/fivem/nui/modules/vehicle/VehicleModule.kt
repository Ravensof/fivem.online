package online.fivem.nui.modules.vehicle

import online.fivem.nui.common.AbstractNuiModule

class VehicleModule : AbstractNuiModule() {

	override fun onInit() {
		moduleLoader.apply {
			add(InternetRadio())
			add(SpeedometerModule(coroutineContext))
		}
	}
}