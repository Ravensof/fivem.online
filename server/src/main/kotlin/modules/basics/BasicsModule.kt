package online.fivem.server.modules.basics

import online.fivem.common.common.AbstractModule
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.net.SpawnEvent
import online.fivem.server.modules.clientEventExchanger.ClientEvent

class BasicsModule : AbstractModule() {
	override fun init() {
		moduleLoader.add(MySQLModule())
		moduleLoader.add(CommandsModule())
	}

	fun spawn(playerSrc: PlayerSrc, coordinatesX: CoordinatesX, pedHash: Int) {
		ClientEvent.emit(SpawnEvent(coordinatesX, pedHash), playerSrc)
	}
}