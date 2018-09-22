package server.structs.tables

data class ConnectionLogTable(
		val id: Int? = null,
		val last_name: String? = null,
		val time: Int? = null,
		val steam: String? = null,
		val license: String? = null,
		val ip: String? = null
) {
	companion object {
		const val TABLE_NAME: String = "connection_log"
		const val FIELD_ID = "id"
		const val FIELD_LAST_NAME = "last_name"
		const val FIELD_STEAM = "steam"
		const val FIELD_LICENSE = "license"
		const val FIELD_IP = "ip"
		const val FIELD_TIME = "time"
	}
}