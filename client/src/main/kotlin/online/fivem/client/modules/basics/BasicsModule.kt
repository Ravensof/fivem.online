package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.events.PauseMenuStateChangedEvent
import online.fivem.client.extensions.addText
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Event
import online.fivem.common.common.Stack
import online.fivem.common.gtav.NativeTextEntries

class BasicsModule : AbstractModule() {

	private val API by moduleLoader.delegate<API>()

	private var handleShowNui = Stack.UNDEFINED_INDEX

	override fun onInit() {
		Event.on<PauseMenuStateChangedEvent> { onPauseMenuStateChanged(it.previousState) }

		moduleLoader.apply {
			add(ErrorReporterModule())
			add(LocalStorageModule())
			add(TickExecutorModule())
			add(ControlHandlerModule())
			add(API())
			add(JoinTransitionModule())
			add(SpawnManagerModule())
			add(DateTimeModule())
			add(WeatherModule())
			add(VoiceTransmissionModule())

			add(ServersCommandsHandlerModule())
		}
	}

	override fun onStart(): Job? {
		changeHeaderInMainMenu()

		return super.onStart()
	}

	override fun onStop(): Job? {
		API.hideNui()

		return super.onStop()
	}

	private fun onPauseMenuStateChanged(previousState: Int) {
		API.cancelHideNui(handleShowNui)
		if (previousState == 0) {
			handleShowNui = API.hideNui()
		}
	}

	private fun changeHeaderInMainMenu() {
		NativeTextEntries.FE_THDR_GTAO.addText(GlobalConfig.SERVER_NAME_IN_MENU)
	}
}