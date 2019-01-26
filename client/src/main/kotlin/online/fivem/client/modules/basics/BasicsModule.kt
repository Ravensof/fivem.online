package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.events.PauseMenuStateChangedEvent
import online.fivem.common.events.net.ShowGuiEvent
import kotlin.coroutines.CoroutineContext

class BasicsModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = Job()

	override fun init() {
		UEvent.on<PauseMenuStateChangedEvent> { onPauseMenuStateChanged(it.pauseMenuState) }

		moduleLoader.apply {
			add(TickExecutorModule())
			add(ControlHandlerModule(coroutineContext))
			add(JoinTransitionModule(coroutineContext))
			add(SpawnManagerModule(coroutineContext))
			add(API(coroutineContext))
		}
	}

	override fun start(): Job? {
		changeHeaderInMainMenu()

		return super.start()
	}

	private fun onPauseMenuStateChanged(state: Int) {
		NuiEvent.emit(ShowGuiEvent(state == 0))
	}

	private fun changeHeaderInMainMenu() {
		Client.addTextEntry("FE_THDR_GTAO", GlobalConfig.SERVER_NAME_IN_MENU)
	}
}