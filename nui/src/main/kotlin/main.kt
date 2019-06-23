import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.nui.modules.basics.BasicsModule
import online.fivem.nui.modules.basics.GUIModule
import online.fivem.nui.modules.client_event_exchanger.ClientEventExchangerModule
import online.fivem.nui.modules.mobile_phone.MobilePhoneModule
import online.fivem.nui.modules.vehicle.VehicleModule
import online.fivem.nui.modules.voice_transmission.VoiceTransmissionModule

private fun main() {
	Console.log("nui side loading..")

	GlobalConfig.concatConsoleOutput = true

	ModuleLoader().apply {
		launch {
			add(BasicsModule())

			val guiModule = GUIModule().also {
				add(it)
			}

			add(VehicleModule(guiModule))

			add(MobilePhoneModule(guiModule))

			add(VoiceTransmissionModule())

			val clientEventExchangerModule = ClientEventExchangerModule().also {
				add(it, manualStart = true)
			}

			startAll()

			start(clientEventExchangerModule)

			Console.log("all modules loaded")
		}
	}
}