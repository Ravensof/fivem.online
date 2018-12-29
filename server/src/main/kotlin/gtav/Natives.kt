package online.fivem.server.gtav

import online.fivem.common.entities.PlayerSrc

object Natives {
	fun on(eventName: String, callback: Any) = online.fivem.server.gtav.on(eventName, callback)

	fun emitNet(eventName: String, playerSrc: Int, data: Any) = emitNet(eventName, playerSrc.toString(), data)

	fun onNet(eventName: String, callback: (PlayerSrc, Any) -> Unit): Unit = Exports.onNet(eventName, callback)

}

private external fun on(eventName: String, callback: Any)

private external fun emitNet(eventName: String, target: String, vararg args: Any)