package online.fivem.server.entities.mysqlEntities

abstract class CharacterWeaponsEntity(
	val character_id: Int,
	val weapon_id: String,
	val count: Int
)