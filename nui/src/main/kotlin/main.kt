package online.fivem.nui

import online.fivem.common.common.Console
import online.fivem.common.common.Html
import online.fivem.common.common.ModuleLoader
import online.fivem.nui.extensions.getResourceLink
import online.fivem.nui.extensions.loadJS
import online.fivem.nui.modules.basics.BasicsModule
import online.fivem.nui.modules.clientEventEchanger.ClientEventExchangerModule
import online.fivem.nui.modules.vehicle.VehicleModule

internal suspend fun main() {
	Console.log("nui side loading..")

	Html.apply {
		val nui = getResourceLink("nui", "")
		val common = getResourceLink("common", "")

		val jsFiles = listOf(
			common + "externalJS/base64Danko.min.js",
			common + "externalJS/sha512.min.js",
			common + "externalJS/common.js",

			nui + "js/howler_2.0.15.core.min.js",
			nui + "js/jquery.scrollTo.min.js"
		)

		jsFiles.forEach {
			Console.log("loading js $it")
			loadJS(it).join()//todo test
		}
	}

	ModuleLoader().apply {

		add(BasicsModule())

		add(VehicleModule())

		add(Test())

		add(ClientEventExchangerModule())//last

		finally {
			Console.log("all modules loaded")
		}
	}.start()
}