package client.modules.eventGenerator.events.vehicle

import universal.events.IEvent

class PlayerJoinVehicleEvent(
		val seatIndex: Int
) : IEvent()