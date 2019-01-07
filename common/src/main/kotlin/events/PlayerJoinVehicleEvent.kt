package online.fivem.common.events

class PlayerJoinVehicleEvent(
	val vehicle: Int,
	val seatIndex: Int
) : PlayerLeftOrJoinVehicleEvent()