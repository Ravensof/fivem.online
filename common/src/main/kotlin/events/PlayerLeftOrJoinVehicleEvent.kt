package online.fivem.common.events

open class PlayerLeftOrJoinVehicleEvent

class PlayerLeftVehicleEvent : PlayerLeftOrJoinVehicleEvent()

class PlayerJoinVehicleEvent(
	val seatIndex: Int
) : PlayerLeftOrJoinVehicleEvent()