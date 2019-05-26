import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.ServerConfig.CURRENT_RESOURCE_NAME
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.basics.BasicsModule
import online.fivem.server.modules.client_event_exchanger.ClientEventExchangerModule
import online.fivem.server.modules.roleplay_system.RolePlaySystemModule
import online.fivem.server.modules.test.Test

internal fun main() {

	if (CURRENT_RESOURCE_NAME != GlobalConfig.MODULE_NAME)
		throw Exception("GlobalConfig.MODULE_NAME should be set in $CURRENT_RESOURCE_NAME")

	Natives.on(NativeEvents.Server.RESOURCE_START) { resourceName: String ->
		if (resourceName == GlobalConfig.MODULE_NAME) {
			start()
		}
	}
}

private fun start() {
	Console.log("server side loading..")

	ModuleLoader().apply {
		launch {
			add(BasicsModule())

			add(RolePlaySystemModule())

			add(Test())

			val clientEventExchangerModule = ClientEventExchangerModule().also {
				add(it, manualStart = true)//last
			}

			startAll()

			start(clientEventExchangerModule)

			Console.log("server side loaded")
		}
	}
}