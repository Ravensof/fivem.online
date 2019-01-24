package online.fivem.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Console
import online.fivem.common.common.ModuleLoader
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.basics.BasicsModule
import online.fivem.server.modules.clientEventExchanger.ClientEventExchangerModule
import online.fivem.server.modules.rolePlaySystem.RolePlaySystemModule
import online.fivem.server.modules.test.Test

internal fun main() {

	Console.log("check configurations..")

	if (Natives.getCurrentResourceName() != GlobalConfig.MODULE_NAME)
		throw Exception("GlobalConfig.MODULE_NAME should be set in ${Natives.getCurrentResourceName()}")

	Natives.on(NativeEvents.Server.RESOURCE_START) { resourceName: String ->
		if (resourceName == GlobalConfig.MODULE_NAME) GlobalScope.launch {
			checkConfigurations(this)

			start()
		}
	}
}

private fun start() {
	Console.log("server side loading..")

	ModuleLoader().apply {

		add(BasicsModule())

		add(RolePlaySystemModule())

		add(Test())

		add(ClientEventExchangerModule())//last

		finally {
			Console.log("server side loaded")
		}
	}.start()
}

private suspend fun checkConfigurations(coroutineScope: CoroutineScope) {
	val fileToCheck = "nui/lib/jquery.js"

	val resource1 = withTimeoutOrNull(30_000) {
		Exports.performHttpRequest(
			coroutineScope,
			GlobalConfig.RESOURCES_HTTP_HOME + fileToCheck
		).await()
	} ?: throw Exception("can't check GlobalConfig.RESOURCES_HTTP_HOME: http server didnt respond")

	val resource2 = Natives.loadResourceFile(GlobalConfig.MODULE_NAME, fileToCheck)

	if (resource1 != resource2)
		throw Exception("error while checking GlobalConfig.RESOURCES_HTTP_HOME, check configurations")

	Console.log("configurations are ok")
}
