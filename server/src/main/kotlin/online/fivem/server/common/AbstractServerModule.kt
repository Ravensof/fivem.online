package online.fivem.server.common

import online.fivem.common.common.AbstractModule
import online.fivem.server.entities.Player

abstract class AbstractServerModule : AbstractModule() {

	open suspend fun onPlayerSave(player: Player, dataList: List<Any>) {}

}