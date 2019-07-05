import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.CustomScope
import online.fivem.common.common.ModuleLoader
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.ServerConfig
import online.fivem.server.ServerConfig.CURRENT_RESOURCE_NAME
import online.fivem.server.gtav.Natives
import online.fivem.server.gtav.enums.ResourceState
import online.fivem.server.modules.basics.BasicsModule
import online.fivem.server.modules.client_event_exchanger.ClientEventExchangerModule
import online.fivem.server.modules.roleplay_system.RolePlaySystemModule
import online.fivem.server.modules.test.Test

private fun main() {
	CustomScope.launch {
		ServerConfig.init()

		if (CURRENT_RESOURCE_NAME != GlobalConfig.MODULE_NAME)
			throw Exception("GlobalConfig.MODULE_NAME should be set in $CURRENT_RESOURCE_NAME")

		Natives.on(NativeEvents.Server.RESOURCE_START) { resourceName: String ->

			if (resourceName == GlobalConfig.MODULE_NAME) {
				start()
			}
		}

		if (Natives.getResourceState(CURRENT_RESOURCE_NAME) == ResourceState.STARTED) {
			start()
		}
	}
}

private fun start() {
	Console.log("server side loading..")

	ModuleLoader().apply {
		launch {
			val basicsModule = BasicsModule().also {
				add(it)
			}

			add(RolePlaySystemModule(basicsModule.mySQLModule))

			val clientEventExchangerModule = ClientEventExchangerModule(basicsModule.sessionModule).also {
				add(it, manualStart = true)//last
			}

			startAll()

			start(clientEventExchangerModule)

			Console.log("server side loaded")
		}
	}
}