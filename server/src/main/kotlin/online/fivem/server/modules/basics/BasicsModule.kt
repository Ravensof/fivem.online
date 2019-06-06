package online.fivem.server.modules.basics

import online.fivem.server.common.AbstractServerModule
import online.fivem.server.modules.basics.mysql.MySQLModule

class BasicsModule : AbstractServerModule() {

	val sessionModule = SessionModule(coroutineContext)
	private val mySQLModule = MySQLModule(coroutineContext)

	override suspend fun onInit() {

		arrayOf(
			mySQLModule,
			CommandsModule(sessionModule),
			HttpServerModule(coroutineContext),
			sessionModule,
			SynchronizationModule(
				mySQLModule = mySQLModule,
				sessionModule = sessionModule
			),
			NatureControlSystemModule(coroutineContext),
			VoiceTransmissionModule(),
			ClientsErrorReportingsModule()

		).forEach {
			moduleLoader.add(it)
		}
	}
}