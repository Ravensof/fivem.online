package fivem.events

import universal.events.IEvent

data class ClientReady(
		val token: String? = null
) : IEvent()