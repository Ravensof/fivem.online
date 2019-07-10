package online.fivem.client.modules.basics

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.extensions.addText
import online.fivem.common.GlobalConfig
import online.fivem.common.extensions.forEach
import online.fivem.common.gtav.NativeTextEntries

class BasicsModules : AbstractClientModule() {

	val tickExecutorModule = TickExecutorModule()

	private val controlHandlerModule = ControlHandlerModule(tickExecutorModule)

	val bufferedActionsModule = BufferedActionsModule(
		tickExecutorModule = tickExecutorModule,
		controlHandlerModule = controlHandlerModule
	)

	val stateRepositoryModule = StateRepositoryModule(
		bufferedActionsModule = bufferedActionsModule
	)

	private val spawnManagerModule = SpawnManagerModule(bufferedActionsModule)

	private val joinTransitionModule = JoinTransitionModule(
		bufferedActionsModule = bufferedActionsModule,
		tickExecutorModule = tickExecutorModule
	)

	private val dateTimeModule = DateTimeModule()

	override suspend fun onInit() {

		moduleLoader.apply {
			add(ErrorReporterModule())
			add(tickExecutorModule)
			add(controlHandlerModule)
			add(bufferedActionsModule)
			add(joinTransitionModule)
			add(spawnManagerModule)
			add(dateTimeModule)
			add(WeatherModule(dateTimeModule))
			add(VoiceTransmissionModule(stateRepositoryModule))
			add(MapModule())

			add(
				ServersCommandsHandlerModule(
					spawnManagerModule = spawnManagerModule,
					joinTransitionModule = joinTransitionModule,
					stateRepositoryModule = stateRepositoryModule
				)
			)
			add(stateRepositoryModule)
		}
	}

	override fun onStartAsync() = async {
		changeHeaderInMainMenu()
		stateRepositoryModule.waitForStart()

		subscribeOnPauseMenuChanged()
	}

	override fun onStop() = launch {
		bufferedActionsModule.hideNui(this@BasicsModules)
	}

	private fun subscribeOnPauseMenuChanged() = launch {
		stateRepositoryModule
			.isPauseMenuEnabled.openSubscription()
			.forEach { isPause ->

				launch {
					if (isPause) {
						bufferedActionsModule.hideNui(this@BasicsModules)
					} else {
						bufferedActionsModule.cancelHideNui(this@BasicsModules)
					}
				}
			}
	}

	private fun changeHeaderInMainMenu() {
		NativeTextEntries.MENU_TITLE.addText(GlobalConfig.SERVER_NAME_IN_MENU)
	}
}