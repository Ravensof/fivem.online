package web

//import shared.struct.HttpRequestType
import org.w3c.dom.events.Event
import universal.common.Console
import web.common.performHttpRequest
import web.struct.HttpRequestType
import kotlin.browser.document

fun main(args: Array<String>) {

//	MODULE_FOLDER_NAME

	document.addEventListener("DOMContentLoaded", fun(event: Event) {
		Console.log("dom content loaded")
	})

//	Console.log("test 12345")

	// http://cms-test.socialsib.net/fivemapi/

	performHttpRequest("http://cms-test.socialsib.net/fivemapi/", HttpRequestType.POST, mapOf("test" to "123")) {
		Console.log("answer received")
		Console.log(it)
	}
}