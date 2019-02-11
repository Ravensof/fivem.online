package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import online.fivem.common.common.AbstractModule
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.server.entities.Player
import online.fivem.server.modules.client_event_exchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class BasicsModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = SupervisorJob()

	override fun onInit() {
		moduleLoader.add(MySQLModule(coroutineContext))
		moduleLoader.add(CommandsModule(coroutineContext))
		moduleLoader.add(HttpServerModule(coroutineContext))
		moduleLoader.add(SessionModule(coroutineContext))

		moduleLoader.add(SynchronizationModule(coroutineContext))

		moduleLoader.add(NatureControlSystemModule(coroutineContext))
	}

	fun spawn(player: Player, coordinatesX: CoordinatesX, pedHash: Int) {
		ClientEvent.emit(SpawnPlayerEvent(coordinatesX, pedHash), player)
	}
}