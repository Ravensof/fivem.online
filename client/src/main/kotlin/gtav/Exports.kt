package online.fivem.client.gtav

import online.fivem.common.GlobalConfig

private external val exports: dynamic

object Exports {
	private val exports = online.fivem.client.gtav.exports[GlobalConfig.MODULE_NAME]

	fun onNui(eventName: String, callback: (Any) -> Unit) {
		exports.onNui(eventName, callback)
	}

	fun emitNet(eventName: String, data: Any) {
		exports.emitNet(eventName, data)
	}
}