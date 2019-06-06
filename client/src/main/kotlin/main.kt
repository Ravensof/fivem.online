import kotlinx.coroutines.launch
import online.fivem.client.gtav.Natives
import online.fivem.client.modules.basics.BasicsModules
import online.fivem.client.modules.eventGenerator.EventGeneratorModule
import online.fivem.client.modules.nui_event_exchanger.NuiEventExchangerModule
import online.fivem.client.modules.role_play_system.RolePlaySystemModule
import online.fivem.client.modules.server_event_exchanger.ServerEventExchangerModule
import online.fivem.client.modules.vehicle.VehicleModule
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
				VehicleModule(
					apiModule = basicsModules.apiModule,
					tickExecutorModule = basicsModules.tickExecutorModule
				)
			)

			add(RolePlaySystemModule(basicsModules.tickExecutorModule))

			add(EventGeneratorModule())//pre last

			val serverExchangerModule = ServerEventExchangerModule().also {
				add(it, manualStart = true)//last
			}

			startAll()

			start(serverExchangerModule)

			Console.log("all modules loaded")
		}
	}
}