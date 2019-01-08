package online.fivem.client.modules.basics

import online.fivem.client.gtav.Client
import online.fivem.client.modules.eventGenerator.TickExecutor
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.client.modules.serverEventExchanger.ServerEvent
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.UEvent
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.PauseMenuStateChangedEvent
import online.fivem.common.events.RequestPackEvent
import online.fivem.common.events.ShowGuiEvent
import online.fivem.common.events.SynchronizeEvent
import kotlin.reflect.KClass

class BasicsModule : AbstractModule() {

	private var menuStateChangeExecutorId = -1

	override fun init() {
		UEvent.on<PauseMenuStateChangedEvent> { onPauseMenuStateChanged(it.pauseMenuState) }
		ServerEvent.on<RequestPackEvent> { onServerRequest(it.kClasses) }
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

	private fun onServerRequest(kClasses: List<KClass<*>>) {

		val response = mutableListOf<Any>()
		val playerPed = Client.getPlayerPed() ?: return Console.warn("no player ped")

		kClasses.forEach { kClass ->
			when (kClass) {
				CoordinatesX::class -> {
					val coordinates =
						Client.getEntityCoords(playerPed) ?: return Console.warn("player ped has no coordinates")

					response.add(
						CoordinatesX(
							coordinates,
							Client.getEntityHeading(playerPed)
						)
					)
				}
			}
		}

		ServerEvent.emit(
			SynchronizeEvent(response)
		)
	}
}