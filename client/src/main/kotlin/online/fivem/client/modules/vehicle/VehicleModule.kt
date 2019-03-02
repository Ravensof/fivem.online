package online.fivem.client.modules.vehicle

import online.fivem.client.common.AbstractClientModule

class VehicleModule : AbstractClientModule() {

	override fun onInit() {
		moduleLoader.apply {
			add(InternetRadio())
			add(Speedometer())
			add(BlackOut())
			add(RealisticFailureModule())
		}
	}
}