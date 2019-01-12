package online.fivem.client.modules.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import online.fivem.client.gtav.Client
import online.fivem.client.modules.serverEventExchanger.ServerEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.SynchronizeEvent
import online.fivem.common.events.net.RequestPackEvent
import online.fivem.common.events.net.SpawnEvent

class SynchronizationModule : AbstractModule() {

	private val spawnManager by moduleLoader.onReady<SpawnManagerModule>()
	private val joinTransition by moduleLoader.onReady<JoinTransitionModule>()

	override fun init() {
		ServerEvent.on<RequestPackEvent> { onServerRequest(it.kClasses) }
		ServerEvent.on<SpawnEvent> { onSpawn(it.coordinatesX, it.model) }
	}

	private fun onSpawn(coordinatesX: CoordinatesX, model: Int) {
		GlobalScope.launch {
			joinTransition.startTransition().join()
			spawnManager.spawnPlayer(coordinatesX, model).join()
			joinTransition.endTransition()
		}
	}

	private fun onServerRequest(kClasses: Array<String>) {

		val response = mutableListOf<Any>()
		val playerPed = Client.getPlayerPed()

		kClasses.forEach { kClass ->
			when (kClass) {
				CoordinatesX::class.simpleName -> {
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
			SynchronizeEvent(response.toTypedArray())
		)
	}
}