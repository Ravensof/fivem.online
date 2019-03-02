import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.nui.modules.basics.BasicsModule
import online.fivem.nui.modules.client_event_exchanger.ClientEventExchangerModule
import online.fivem.nui.modules.test.Test
import online.fivem.nui.modules.vehicle.VehicleModule
import online.fivem.nui.modules.voice_transmission.VoiceTransmissionModule

internal fun main() {
	Console.log("nui side loading..")

	ModuleLoader().apply {

		add(BasicsModule())

		add(VehicleModule())

		add(VoiceTransmissionModule())

		add(ClientEventExchangerModule())//last

		finally {
			Console.log("all modules loaded")
		}
	}.start()
}