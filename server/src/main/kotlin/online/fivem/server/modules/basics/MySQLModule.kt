package online.fivem.server.modules.basics

import external.nodejs.mysql.Connection
import external.nodejs.mysql.Params
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import require
import kotlin.coroutines.CoroutineContext

class MySQLModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	lateinit var connection: Connection
		private set

	private val mysql = require("mysql").unsafeCast<external.nodejs.mysql.MySQL>()

	override fun onInit() {
		connect()
	}

	override fun onStop(): Job? {
		connection.end()

		return super.onStop()
	}

	private fun connect() {
		connection = mysql.createConnection(
			Params(
				host = "127.0.0.1",
				database = "fivem",
				user = "fivem",
				password = "qwerta"
			)
		)

		connection.on("error") { error ->
			if (error?.code == "PROTOCOL_CONNECTION_LOST") {
				connect()
			} else {
				throw Exception(error.toString())
			}
		}

		connection.connect()
	}
}