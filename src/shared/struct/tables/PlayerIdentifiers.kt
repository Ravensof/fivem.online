package shared.struct.tables

data class PlayerIdentifiers(
		val id: Int? = null,
		val steam: String? = null,
		val license: String? = null,
		val ip: String? = null
) : AbstractTable() {
	override val tableName: String = "player_identifiers"
}