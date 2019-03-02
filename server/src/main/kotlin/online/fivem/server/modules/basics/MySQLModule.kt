package online.fivem.server.modules.basics

import external.nodejs.mysql.Connection
import external.nodejs.require
import kotlinx.coroutines.Job
import online.fivem.server.ServerConfig.MYSQL_PARAMS
import online.fivem.server.common.AbstractServerModule
import kotlin.coroutines.CoroutineContext

class MySQLModule(override val coroutineContext: CoroutineContext) : AbstractServerModule() {

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
		connection = mysql.createConnection(MYSQL_PARAMS)

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