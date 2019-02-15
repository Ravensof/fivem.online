package online.fivem.server

import external.nodejs.mysql.Params
import online.fivem.common.GlobalConfig
import online.fivem.server.gtav.Natives

object ServerConfig {

	const val SYNCHRONIZATION_PERIOD_SECONDS = GlobalConfig.MAX_PLAYERS
	const val KICK_FOR_PACKET_OVERFLOW = true

	val MYSQL_PARAMS = Params(
		host = "127.0.0.1",
		database = "fivem",
		user = "fivem",
		password = "qwerta"
	)

	val CURRENT_RESOURCE_NAME = Natives.getCurrentResourceName()
	val CURRENT_RESOURCE_PATH = Natives.getResourcePath(CURRENT_RESOURCE_NAME) + "/"
}