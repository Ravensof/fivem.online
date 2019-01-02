package online.fivem.common.events

open class PlayerSeatChangedEvent(
	val seatIndex: Int
)

class PlayerGetInPassengerSeatEvent(seatIndex: Int) : PlayerSeatChangedEvent(seatIndex)

class PlayerGetInDriversSeatEvent : PlayerSeatChangedEvent(-1)