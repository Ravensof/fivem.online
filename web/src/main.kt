import universal.common.Console
import web.common.Event
import web.modules.gui.GuiModule
import web.modules.radio.RadioModule
import web.modules.speedometer.SpeedometerModule

fun main(args: Array<String>) {

	try {

		Event.init()

//	Event.onNui<ConsoleLogWeb> {  }

		RadioModule.getInstance()

		SpeedometerModule.getInstance()
		GuiModule.getInstance()

//	Console.log("test 12345")


//	performHttpRequest("http://cms-test.socialsib.net/fivemapi/", HttpRequestType.POST, mapOf("test" to "123")) {
//		Console.log("answer received")
//		Console.log(it)
//	}

		Console.info("web gui started")
	} catch (exception: Exception) {
		Console.logWeb(exception.message)
		Console.error(exception.message)
	}
}