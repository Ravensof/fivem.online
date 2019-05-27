package online.fivem.server.modules.basics.mysql

import external.nodejs.mysql.Pool
import external.nodejs.require
import online.fivem.server.ServerConfig.MYSQL_PARAMS
import online.fivem.server.common.AbstractServerModule
import kotlin.coroutines.CoroutineContext

class MySQLModule(override val coroutineContext: CoroutineContext) : AbstractServerModule() {

	val pool: Pool by lazy { mysql.createPool(MYSQL_PARAMS) }

	private val mysql = require("mysql").unsafeCast<external.nodejs.mysql.MySQL>()

	override suspend fun onInit() {
		testConnection()
	}

	private fun testConnection() {
		val connection = mysql.createConnection(MYSQL_PARAMS)

//		connection.on("error") { error ->
//			if (error?.code == "PROTOCOL_CONNECTION_LOST") {
//				Console.info("MySQL Module: PROTOCOL_CONNECTION_LOST ... reconnecting")
//				testConnection()
//			} else {
//				throw Exception("MySQL Module: " + error.toString())
//			}
//		}

		connection.connect()
		connection.end()
	}
}