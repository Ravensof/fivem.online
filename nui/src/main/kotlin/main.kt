package online.fivem.nui

import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.nui.modules.basics.BasicsModule
import online.fivem.nui.modules.clientEventEchanger.ClientEventExchangerModule
import online.fivem.nui.modules.loadingScreen.LoadingScreenModule
import online.fivem.nui.modules.vehicle.VehicleModule

internal fun main() {
	Console.log("nui side loading..")

	ModuleLoader().apply {

		add(BasicsModule())

		add(VehicleModule())

		add(ClientEventExchangerModule())//last

//		add(Test())

		finally {
			Console.log("all modules loaded")
		}
	}.start()
}