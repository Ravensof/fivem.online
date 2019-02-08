package online.fivem.common.events

sealed class PlayersPedHealthChangedEvent(
	val health: Int,
	val diff: Int
) {

	class Dropped(
		health: Int,
		diff: Int
	) : PlayersPedHealthChangedEvent(health, diff)

	class Increased(
		health: Int,
		diff: Int
	) : PlayersPedHealthChangedEvent(health, diff)

	class Zero(
		diff: Int
	) : PlayersPedHealthChangedEvent(0, diff)
}