package online.fivem.common.events.local

class PlayerJoinVehicleEvent(
	val vehicle: Int,
	val seatIndex: Int
) : PlayerLeftOrJoinVehicleEvent()