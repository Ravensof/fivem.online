package client.modules.eventGenerator.events.vehicle

import universal.events.IEvent

class PlayerSeatChangedEvent(
		val seatIndex: Int?
) : IEvent()