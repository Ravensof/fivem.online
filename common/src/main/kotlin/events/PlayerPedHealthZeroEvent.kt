package online.fivem.common.events

class PlayerPedHealthZeroEvent(
	lastHealth: Int
) : PlayersPedHealthDropppedEvent(0, lastHealth)