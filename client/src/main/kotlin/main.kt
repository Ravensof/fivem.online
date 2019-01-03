package online.fivem.client

import online.fivem.client.gtav.Natives
import online.fivem.client.modules.controlManager.ControlManagerModule
import online.fivem.client.modules.eventGenerator.EventGenerator
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.client.modules.nuiEventExchanger.NuiEventExchangerModule
import online.fivem.client.modules.serverEventExchanger.ServerEventExchangerModule
import online.fivem.client.modules.vehicle.VehicleModule
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.common.events.HideLoadingScreen
import online.fivem.common.gtav.NativeEvents

internal fun main(args: Array<String>) {

	Natives.on(NativeEvents.Client.RESOURCE_START) { resource: String ->
		if (resource == GlobalConfig.MODULE_NAME) {
			start()
		}
	}
}

fun start() {
	Console.log("client side loading..")

	ModuleLoader().apply {

		add(NuiEventExchangerModule())//first

		add(VehicleModule())

		add(ControlManagerModule())

		add(EventGenerator())

		add(ServerEventExchangerModule())//last

//		add(Test())

		finally {
			NuiEvent.emit(HideLoadingScreen())

			Console.log("all modules loaded")
		}
	}.start()
}