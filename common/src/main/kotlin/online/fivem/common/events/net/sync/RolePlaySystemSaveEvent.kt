package online.fivem.common.events.net.sync

import online.fivem.common.entities.CoordinatesX
import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class RolePlaySystemSaveEvent(
	val coordinatesX: CoordinatesX
) : Serializable()