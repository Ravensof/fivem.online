package online.fivem.server.modules.basics

import external.nodejs.mysql.Connection
import online.fivem.common.common.Console
import online.fivem.common.events.net.ErrorReportEvent
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.entities.Player
import online.fivem.server.extensions.sendAsync
import online.fivem.server.modules.client_event_exchanger.ClientEvent

class ClientsErrorReportingsModule : AbstractServerModule() {

	private lateinit var mysql: Connection

	init {
		ClientEvent.on(::onErrorReport)
	}

	override fun onInit() {
		moduleLoader.on<MySQLModule> { mysql = it.connection }

		super.onInit()
	}

	private fun onErrorReport(player: Player, event: ErrorReportEvent) {
		Console.debug("crash report from user ${player.userId}:\r\n" + event.message)

		mysql.sendAsync(
			"""
			|INSERT INTO crash_reports
			|SET
			|   user_id=?,
			|   time=NOW(),
			|   message=?
			|
		""".trimMargin(),
			arrayOf(
				player.userId,
				event.message
			)
		)
	}
}