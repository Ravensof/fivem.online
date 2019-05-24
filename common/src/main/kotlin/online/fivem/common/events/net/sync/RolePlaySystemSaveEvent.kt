package online.fivem.common.events.net.sync

import online.fivem.common.entities.CoordinatesX
import online.fivem.common.other.Serializable

class RolePlaySystemSaveEvent(
	val coordinatesX: CoordinatesX
) : Serializable()