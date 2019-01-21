package online.fivem.client

import online.fivem.client.gtav.Natives
import online.fivem.client.modules.basics.BasicsModule
import online.fivem.client.modules.eventGenerator.EventGeneratorModule
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.client.modules.nuiEventExchanger.NuiEventExchangerModule
import online.fivem.client.modules.serverEventExchanger.ServerEventExchangerModule
import online.fivem.client.modules.test.Test
import online.fivem.client.modules.vehicle.VehicleModule
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.common.events.HideLoadingScreenEvent
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

		add(BasicsModule())

		add(VehicleModule())

		add(EventGeneratorModule())

		add(ServerEventExchangerModule())//last

		add(Test())

		finally {
			NuiEvent.emit(HideLoadingScreenEvent())

			Console.log("all modules loaded")
		}
	}.start()
}