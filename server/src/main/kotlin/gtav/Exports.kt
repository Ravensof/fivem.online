package online.fivem.server.gtav

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Html
import online.fivem.common.entities.PlayerSrc

private external val exports: dynamic

object Exports {
	private val exports = online.fivem.server.gtav.exports[GlobalConfig.MODULE_NAME]

	fun performHttpRequest(
		url: String,
		httpRequestType: String = "GET",
		data: Map<String, String>? = null,
		headers: Any = object {}
	): Deferred<String> {

		val postData =
			data?.map { Html.urlEncode(it.key) + "=" + Html.urlEncode(it.value) }?.joinToString("&").orEmpty()

		val channel = Channel<String>()

		exports.performHttpRequest(url, { _: Int, response: String, _: Any ->
			GlobalScope.launch {
				channel.send(response)
			}
			Unit
		}, httpRequestType, postData, headers)

		return GlobalScope.async {
			channel.receive()
		}
	}

	fun onNet(eventName: String, callback: (PlayerSrc, Any) -> Unit) {
		exports.onNet(eventName) { playerId: Int, data: Any ->
			callback(PlayerSrc(playerId), data)
		}
	}

	fun on(eventName: String, callback: Any) {
		exports.on(eventName, callback)
	}

	fun registerCommand(command: String, isProtected: Boolean = true, callback: (Int, Array<String>, String) -> Unit) {
		exports.registerCommand(command, isProtected, callback)
	}
}