package online.fivem.server.modules.basics

import online.fivem.server.common.AbstractServerModule
import online.fivem.server.modules.basics.mysql.MySQLModule

class BasicsModule : AbstractServerModule() {

	val mySQLModule = MySQLModule()
	val sessionModule = SessionModule(mySQLModule)

	override suspend fun onInit() {

		arrayOf(
			mySQLModule,
			CommandsModule(sessionModule),
			HttpServerModule(),
			sessionModule,
			SynchronizationModule(
				mySQLModule = mySQLModule,
				sessionModule = sessionModule
			),
			NatureControlSystemModule(),
			VoiceTransmissionModule(),
			ClientsErrorReportingsModule()

		).forEach {
			moduleLoader.add(it)
		}
	}
}