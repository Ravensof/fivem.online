package online.fivem.server.modules.basics

import external.nodejs.mysql.Pool
import kotlinx.coroutines.async
import online.fivem.common.common.Console
import online.fivem.common.events.net.ErrorReportEvent
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.entities.Player
import online.fivem.server.modules.basics.mysql.MySQLModule
import online.fivem.server.modules.basics.mysql.extensions.getConnection
import online.fivem.server.modules.basics.mysql.extensions.send
import online.fivem.server.modules.client_event_exchanger.ClientEvent

class ClientsErrorReportingsModule : AbstractServerModule() {

	private lateinit var mySQL: Pool

	init {
		ClientEvent.on<ErrorReportEvent> { player, event ->
			onErrorReport(player, event)
		}
	}

	override fun onStartAsync() = async {
		mySQL = moduleLoader.getModule(MySQLModule::class).pool
	}

	private suspend fun onErrorReport(player: Player, event: ErrorReportEvent) {
		Console.debug("crash report from user ${player.userId}:\r\n" + event.message)

		mySQL.getConnection().send(
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