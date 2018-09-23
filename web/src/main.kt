//import shared.struct.HttpRequestType

import universal.common.Console
import web.common.Event
import web.common.performHttpRequest
import web.modules.radio.RadioModule
import web.struct.HttpRequestType

fun main(args: Array<String>) {

	Event.init()

	RadioModule.getInstance()

//	Console.log("test 12345")

	// http://cms-test.socialsib.net/fivemapi/

	performHttpRequest("http://cms-test.socialsib.net/fivemapi/", HttpRequestType.POST, mapOf("test" to "123")) {
		Console.log("answer received")
		Console.log(it)
	}
}