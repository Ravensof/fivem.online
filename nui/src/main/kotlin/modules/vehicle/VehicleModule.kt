package online.fivem.nui.modules.vehicle

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule

class VehicleModule : AbstractModule() {
	override fun start(): Job? {
		return GlobalScope.launch {
			InternetRadio().start()?.join()
			Speedometer().start()?.join()
		}
	}
}