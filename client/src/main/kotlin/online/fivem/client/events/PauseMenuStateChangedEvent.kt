package online.fivem.client.events

sealed class PauseMenuStateChangedEvent(
	val currentState: Int,
	val previousState: Int
) {
	class Disabled(previousState: Int) : PauseMenuStateChangedEvent(0, previousState)

	class Enabled(currentState: Int) : PauseMenuStateChangedEvent(currentState, 0)

	class Switched(pauseMenuState: Int, previousState: Int) : PauseMenuStateChangedEvent(pauseMenuState, previousState)
}