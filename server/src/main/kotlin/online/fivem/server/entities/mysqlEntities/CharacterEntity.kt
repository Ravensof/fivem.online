package online.fivem.server.entities.mysqlEntities

abstract class CharacterEntity(
	val id: Int,
	val coord_x: Double,
	val coord_y: Double,
	val coord_z: Double,
	val coord_rotation: Float,
	val pedestrian: Int,
	val health: Int,
	val armour: Int
)