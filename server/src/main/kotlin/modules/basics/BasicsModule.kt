package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import online.fivem.common.common.AbstractModule
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.net.SpawnPlayerEvent
import online.fivem.server.modules.clientEventExchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class BasicsModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = SupervisorJob()

	override fun init() {
		moduleLoader.add(MySQLModule(coroutineContext))
		moduleLoader.add(CommandsModule())
		moduleLoader.add(HttpServerModule(coroutineContext))
		moduleLoader.add(NuiFileShareModule(coroutineContext))
		moduleLoader.add(SessionModule(coroutineContext))
	}

	fun spawn(playerSrc: PlayerSrc, coordinatesX: CoordinatesX, pedHash: Int) {
		ClientEvent.emit(SpawnPlayerEvent(coordinatesX, pedHash), playerSrc)
	}
}