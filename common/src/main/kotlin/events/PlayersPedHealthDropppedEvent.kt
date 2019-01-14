package online.fivem.common.events

open class PlayersPedHealthDropppedEvent(
	health: Int,
	diff: Int
) : PlayersPedHealthChangedEvent(health, diff)