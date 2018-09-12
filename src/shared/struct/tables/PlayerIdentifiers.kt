package shared.struct.tables

data class PlayerIdentifiers(
		val id: Int? = null,
		val last_name: String? = null,
		val steam: String? = null,
		val license: String? = null,
		val ip: String? = null
) : AbstractTable {
	companion object {
		const val TABLE_NAME: String = "player_identifiers"
		const val FIELD_ID = "id"
		const val FIELD_LAST_NAME = "last_name"
		const val FIELD_STEAM = "steam"
		const val FIELD_LICENSE = "license"
		const val FIELD_IP = "ip"
	}
}