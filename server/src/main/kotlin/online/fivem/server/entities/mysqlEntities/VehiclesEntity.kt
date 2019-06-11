package online.fivem.server.entities.mysqlEntities

abstract class VehiclesEntity(
	val id: Int,
	val network_id: Int,
	val character_id: Int,
	val model_hash: Int,
	val location_id: Int,
	val coord_x: Double,
	val coord_y: Double,
	val coord_z: Double,
	val coord_rotation: Float,
	val body_health: Double,
	val engine_health: Double,
	val petrol_tank_health: Double,
	val number_plate: String
)