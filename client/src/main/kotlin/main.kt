import kotlinx.coroutines.launch
import online.fivem.client.gtav.Natives
import online.fivem.client.modules.basics.BasicsModules
import online.fivem.client.modules.nui_event_exchanger.NuiEventExchangerModule
import online.fivem.client.modules.role_play_system.RolePlaySystemModule
import online.fivem.client.modules.server_event_exchanger.ServerEventExchangerModule
import online.fivem.client.modules.test.Test
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.common.gtav.NativeEvents

private fun main() {

	Natives.on(NativeEvents.Client.RESOURCE_START) { resource: String ->
		if (resource == GlobalConfig.MODULE_NAME) {
			start()
		}
	}
}

private fun start() {
	Console.log("client side loading..")

	ModuleLoader().apply {
		launch {

			add(NuiEventExchangerModule())

			val basicsModules = BasicsModules().also {
				add(it)
			}

			add(
				RolePlaySystemModule(
					bufferedActionsModule = basicsModules.bufferedActionsModule,
					tickExecutorModule = basicsModules.tickExecutorModule,
					stateRepositoryModule = basicsModules.stateRepositoryModule
				)
			)

			val serverExchangerModule = ServerEventExchangerModule().also {
				add(it, manualStart = true)//last
			}

			add(Test())

			startAll()

			start(serverExchangerModule)

			Console.log("all modules loaded")
		}
	}
}