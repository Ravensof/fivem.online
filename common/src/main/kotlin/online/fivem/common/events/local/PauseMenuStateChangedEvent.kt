package online.fivem.common.events.local

sealed class PauseMenuStateChangedEvent(
	val pauseMenuState: Int
) {
	class Disabled : PauseMenuStateChangedEvent(0)

	class Switched(pauseMenuState: Int) : PauseMenuStateChangedEvent(pauseMenuState)
}