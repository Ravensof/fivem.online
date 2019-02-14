package online.fivem.client.events

sealed class PauseMenuStateChangedEvent(
	val pauseMenuState: Int
) {
	class Disabled : PauseMenuStateChangedEvent(0)

	class Switched(pauseMenuState: Int) : PauseMenuStateChangedEvent(pauseMenuState)
}