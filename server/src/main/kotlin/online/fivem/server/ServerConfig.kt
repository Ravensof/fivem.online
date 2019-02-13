package online.fivem.server

import external.nodejs.mysql.Params
import online.fivem.common.GlobalConfig

object ServerConfig {
	const val MYSQL_HTTP_API = "http://localhost/fivemapi/mysql.php"
	const val SYNCHRONIZATION_PERIOD_SECONDS = GlobalConfig.MAX_PLAYERS
	const val KICK_FOR_PACKET_OVERFLOW = true

	val MYSQL_PARAMS = Params(
		host = "127.0.0.1",
		database = "fivem",
		user = "fivem",
		password = "qwerta"
	)
}