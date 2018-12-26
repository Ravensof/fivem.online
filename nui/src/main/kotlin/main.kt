package online.fivem.nui

import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.nui.modules.clientEventEchanger.ClientEventExchanger
import online.fivem.nui.modules.loadingScreen.LoadingScreenModule

internal fun main() {
	Console.log("nui side loading..")

//    val body = jQuery("body")
//    val bodyBlock = BodyBlock(body)
//    bodyBlock.addView(TestView())

//    window.performance.now()

//    jq {
//        FancyLines().run()
//    }

	ModuleLoader().apply {

		add(ClientEventExchanger())

		add(LoadingScreenModule())

		finally {
			Console.log("all modules loaded")
		}
	}.start()
}