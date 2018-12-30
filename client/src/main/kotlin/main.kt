package online.fivem.client

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Natives
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.client.modules.nuiEventExchanger.NuiEventExchanger
import online.fivem.client.modules.serverEventExchanger.ServerEventExchanger
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.common.events.HideLoadingScreen
import online.fivem.common.gtav.NativeEvents

internal fun main(args: Array<String>) {

	var resourceLoaded = false

	GlobalScope.launch {
		delay(10_000)
		if (!resourceLoaded) {
			Console.warn("client side start manual loading after 10 seconds..")
			start()
		}
	}

	Natives.on(NativeEvents.Client.RESOURCE_START) { resource: String ->
		if (resource == GlobalConfig.MODULE_NAME) {
			resourceLoaded = true
			start()
		}
	}
}

fun start() {
	Console.log("client side loading..")

	ModuleLoader().apply {
		add(NuiEventExchanger())

		add(ServerEventExchanger())

//		add(Test())

		finally {
			NuiEvent.emit(HideLoadingScreen())

			Console.log("all modules loaded")
		}
	}.start()
}