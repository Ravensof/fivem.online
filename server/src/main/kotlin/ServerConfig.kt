package online.fivem.server

import online.fivem.common.GlobalConfig

object ServerConfig {
	const val MYSQL_HTTP_API = "http://localhost/fivemapi/mysql.php"
	const val SYNCHRONIZATION_PERIOD_SECONDS = GlobalConfig.MAX_PLAYERS
}