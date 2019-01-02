package online.fivem.client

import online.fivem.client.gtav.Natives
import online.fivem.client.modules.controlManager.ControlManager
import online.fivem.client.modules.eventGenerator.EventGenerator
import online.fivem.client.modules.internetRadio.InternetRadio
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.client.modules.nuiEventExchanger.NuiEventExchanger
import online.fivem.client.modules.serverEventExchanger.ServerEventExchanger
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

		add(NuiEventExchanger())

		add(InternetRadio())

		add(ControlManager())

		add(EventGenerator())

		add(ServerEventExchanger())

//		add(Test())

		finally {
			NuiEvent.emit(HideLoadingScreen())

			Console.log("all modules loaded")
		}
	}.start()
}