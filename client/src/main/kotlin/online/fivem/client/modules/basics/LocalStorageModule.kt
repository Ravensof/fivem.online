package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.common.AbstractModule
import online.fivem.common.events.nui.LocalStorageEvent
import kotlin.coroutines.CoroutineContext

private typealias Data = String?

class LocalStorageModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val pendingChannels = mutableMapOf<Int, Channel<Data>>()

	override fun onInit() {
		NuiEvent.on<LocalStorageEvent.Response>(this) {
			pendingChannels[it.responseId]?.send(it.data)
		}
	}

	suspend fun get(key: String): String? {

		val channel = Channel<Data>()
		val requestId = channel.hashCode()

		pendingChannels[requestId] = channel

		channel.hashCode()

		NuiEvent.emit(
			LocalStorageEvent.Request(
				requestId = requestId,
				key = key
			)
		)

		try {
			return channel.receive()
		} catch (exception: Throwable) {
			throw exception
		} finally {
			pendingChannels.remove(requestId)
			channel.close()
		}
	}

	fun getAsync(key: String) = async {
		return@async get(key)
	}

	fun set(key: String, value: String) = launch {
		NuiEvent.emit(
			LocalStorageEvent.Post(
				key = key,
				value = value
			)
		)
	}
}