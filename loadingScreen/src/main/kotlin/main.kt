import kotlinx.coroutines.launch
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.loadingScreen.modules.basics.BasicsModule

private fun main() {
	Console.debug("loading screen loading..")

	ModuleLoader().apply {
		launch {

			add(BasicsModule())

			startAll()

			Console.log("loading screen loaded")
		}
	}
}