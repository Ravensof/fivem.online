package online.fivem.common.events

open class PlayersPedHealthIncreasedEvent(
	health: Int,
	diff: Int
) : PlayersPedHealthChangedEvent(health, diff)