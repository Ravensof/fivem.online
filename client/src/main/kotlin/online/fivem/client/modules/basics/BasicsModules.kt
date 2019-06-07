package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.events.PauseMenuStateChangedEvent
import online.fivem.client.extensions.addText
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Event
import online.fivem.common.gtav.NativeTextEntries

class BasicsModules : AbstractClientModule() {

	val tickExecutorModule = TickExecutorModule()

	private val controlHandlerModule = ControlHandlerModule(tickExecutorModule)

	val apiModule = BufferedActionsModule(
		tickExecutorModule = tickExecutorModule,
		controlHandlerModule = controlHandlerModule
	)

	private val spawnManagerModule = SpawnManagerModule(apiModule)

	private val joinTransitionModule = JoinTransitionModule(
		bufferedActionsModule = apiModule,
		tickExecutorModule = tickExecutorModule
	)

	private val dateTimeModule = DateTimeModule()

	override suspend fun onInit() {
		Event.on<PauseMenuStateChangedEvent> { onPauseMenuStateChanged(it.previousState) }

		moduleLoader.apply {
			add(ErrorReporterModule())
			add(tickExecutorModule)
			add(controlHandlerModule)
			add(apiModule)
			add(joinTransitionModule)
			add(spawnManagerModule)
			add(dateTimeModule)
			add(WeatherModule(dateTimeModule))
			add(VoiceTransmissionModule())

			add(
				ServersCommandsHandlerModule(
					spawnManagerModule = spawnManagerModule,
					joinTransitionModule = joinTransitionModule
				)
			)
		}
	}

	override fun onStart(): Job? {
		changeHeaderInMainMenu()

		return super.onStart()
	}

	override fun onStop() = launch {
		apiModule.hideNui(this@BasicsModules)
	}

	private fun onPauseMenuStateChanged(previousState: Int) = launch {
		if (previousState == 0) {
			apiModule.hideNui(this@BasicsModules)
		} else {
			apiModule.cancelHideNui(this@BasicsModules)
		}
	}

	private fun changeHeaderInMainMenu() {
		NativeTextEntries.FE_THDR_GTAO.addText(GlobalConfig.SERVER_NAME_IN_MENU)
	}
}