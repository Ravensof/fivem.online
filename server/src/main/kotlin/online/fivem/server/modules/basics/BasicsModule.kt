package online.fivem.server.modules.basics

import online.fivem.server.common.AbstractServerModule
import online.fivem.server.modules.basics.mysql.MySQLModule

class BasicsModule : AbstractServerModule() {

	override fun onInit() {
		arrayOf(
			MySQLModule(coroutineContext),
			CommandsModule(coroutineContext),
			HttpServerModule(coroutineContext),
			SessionModule(coroutineContext),
			SynchronizationModule(coroutineContext),
			NatureControlSystemModule(coroutineContext),
			VoiceTransmissionModule(),
			ClientsErrorReportingsModule()

		).forEach {
			moduleLoader.add(it)
		}
	}
}