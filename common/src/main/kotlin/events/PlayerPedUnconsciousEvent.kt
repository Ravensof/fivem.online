package online.fivem.common.events

class PlayerPedUnconsciousEvent(
	lastHealth: Int
) : PlayersPedHealthDropppedEvent(0, lastHealth)