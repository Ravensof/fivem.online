package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import online.fivem.client.gtav.Client
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Stack
import online.fivem.common.common.UEvent
import online.fivem.common.events.PauseMenuStateChangedEvent
import kotlin.coroutines.CoroutineContext

class BasicsModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = SupervisorJob()
	private val API by moduleLoader.onReady<API>()

	private var handleShowNui = Stack.UNDEFINED_INDEX

	override fun onInit() {
		UEvent.on<PauseMenuStateChangedEvent> { onPauseMenuStateChanged(it.pauseMenuState) }

		moduleLoader.apply {
			add(TickExecutorModule())
			add(ControlHandlerModule(coroutineContext))
			add(API(coroutineContext))
			add(JoinTransitionModule(coroutineContext))
			add(SpawnManagerModule(coroutineContext))
			add(DateTimeModule())
			add(WeatherModule(coroutineContext))
			add(VoiceTransmissionModule(coroutineContext))

			add(SynchronizationModule())
		}
	}

	override fun onStart(): Job? {
		changeHeaderInMainMenu()

		return super.onStart()
	}

	private fun onPauseMenuStateChanged(state: Int) {
		API.cancelHideNui(handleShowNui)
		if (state != 0) {
			handleShowNui = API.hideNui()
		}
	}

	private fun changeHeaderInMainMenu() {
		Client.addTextEntry("FE_THDR_GTAO", GlobalConfig.SERVER_NAME_IN_MENU)
	}
}