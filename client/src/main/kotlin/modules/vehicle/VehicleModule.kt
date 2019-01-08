package online.fivem.client.modules.vehicle

import online.fivem.common.common.AbstractModule

class VehicleModule : AbstractModule() {

	override fun init() {
		moduleLoader.apply {
			add(InternetRadio())
			add(Speedometer())
			add(BlackOut())
		}
	}
}