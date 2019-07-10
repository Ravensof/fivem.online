package online.fivem.server

import external.nodejs.mysql.Params
import online.fivem.Natives

object ServerConfig {

	const val SYNCHRONIZATION_PERIOD_SECONDS = GlobalConfig.MAX_PLAYERS
	const val KICK_FOR_PACKET_OVERFLOW = true

	val MYSQL_PARAMS = Params(
		host = "127.0.0.1",
		database = "fivem",
		user = "fivem",
		password = "qwerta"
	)

	lateinit var CURRENT_RESOURCE_NAME: String
		private set

	lateinit var CURRENT_RESOURCE_PATH: String
		private set

	suspend fun init() {
		CURRENT_RESOURCE_NAME = Natives.getCurrentResourceName()
		CURRENT_RESOURCE_PATH = Natives.getResourcePath(CURRENT_RESOURCE_NAME) + "/"
	}
}