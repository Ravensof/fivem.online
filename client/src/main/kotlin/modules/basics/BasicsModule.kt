package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Player
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.client.modules.serverEventExchanger.ServerEvent
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
	override fun start(): Job? {
		UEvent.on<PauseMenuStateChangedEvent> {
			NuiEvent.emit(ShowGuiEvent(it.pauseMenuState == 0))
		}

		ServerEvent.on<RequestPackEvent> { onServerRequest(it.kClasses) }

		return super.start()
	}

	private fun onServerRequest(kClasses: List<KClass<*>>) {

		val response = mutableListOf<Any>()
		val playerPed = Player.getPed() ?: return Console.warn("no player ped")

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