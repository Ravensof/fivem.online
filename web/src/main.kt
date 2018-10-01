import universal.common.Console
import universal.events.NuiReadyEvent
import universal.modules.gui.events.WebReceiverReady
import web.common.Event
import web.modules.gui.GuiModule
import web.modules.radio.RadioModule
import web.modules.speedometer.SpeedometerModule
import web.modules.test.TestModule

fun start() {

	Event.init()

	RadioModule.getInstance()

	SpeedometerModule.getInstance()
	GuiModule.getInstance()

	TestModule.getInstance()

	Event.emitNui(NuiReadyEvent())

	Console.info("web gui started")
}

fun main(args: Array<String>) {

	Event.onNui<WebReceiverReady> {
		MODULE_FOLDER_NAME = it.moduleFolderName
		RESOURCES_URL = it.resourcesURL

		try {
			start()
		} catch (exception: Exception) {
			Console.logWeb(exception.message)
			Console.error(exception.message)
		}
	}
}