package online.fivem.common.events

class PlayerPedUnconsciousEvent(
	lastHealth: Int
) : PlayersPedHealthChangedEvent(0, lastHealth)