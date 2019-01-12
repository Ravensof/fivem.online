package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.gtav.Client
import online.fivem.client.modules.eventGenerator.TickExecutor
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.events.PauseMenuStateChangedEvent
import online.fivem.common.events.ShowGuiEvent

class BasicsModule : AbstractModule() {

	private var menuStateChangeExecutorId = -1

	override fun init() {
		UEvent.on<PauseMenuStateChangedEvent> { onPauseMenuStateChanged(it.pauseMenuState) }

		moduleLoader.apply {
			add(JoinTransitionModule())
			add(SpawnManagerModule())
			add(SynchronizationModule())
		}
	}

	override fun start(): Job? {
		changeHeaderInMainMenu()

		return super.start()
	}

	private fun onPauseMenuStateChanged(state: Int) {
		NuiEvent.emit(ShowGuiEvent(state == 0))

		if (state != 0 && menuStateChangeExecutorId != -1) return

		if (state == 0) {
			TickExecutor.removeTick(menuStateChangeExecutorId)
			menuStateChangeExecutorId = -1
		} else {
			menuStateChangeExecutorId = TickExecutor.addTick(::changeHeaderInMainMenu)
		}
	}

	private fun changeHeaderInMainMenu() {
		Client.addTextEntry("FE_THDR_GTAO", GlobalConfig.SERVER_NAME_IN_MENU)
	}
}