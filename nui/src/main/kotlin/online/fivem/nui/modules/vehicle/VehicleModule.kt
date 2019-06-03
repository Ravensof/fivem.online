package online.fivem.nui.modules.vehicle

import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.modules.basics.GUIModule

class VehicleModule(
	private val guiModule: GUIModule
) : AbstractNuiModule() {

	override suspend fun onInit() {
		moduleLoader.apply {
			add(InternetRadioModule())
			add(SpeedometerModule(guiModule))
		}
	}
}