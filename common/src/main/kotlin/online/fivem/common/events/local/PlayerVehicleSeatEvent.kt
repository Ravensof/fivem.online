package online.fivem.common.events.local

sealed class PlayerVehicleSeatEvent {

	sealed class Join(val seatIndex: Int) : PlayerVehicleSeatEvent() {
		class Passenger(seatIndex: Int) : Join(seatIndex)
		class Driver : Join(-1)
	}

	class Left(val seatIndex: Int) : PlayerVehicleSeatEvent()
	sealed class Changed(
		val previousSeatIndex: Int,
		val newSeatIndex: Int
	) : PlayerVehicleSeatEvent() {

		class AsDriver(
			previousSeatIndex: Int
		) : Changed(previousSeatIndex, -1)

		class AsPassenger(
			previousSeatIndex: Int,
			newSeatIndex: Int
		) : Changed(previousSeatIndex, newSeatIndex)
	}
}