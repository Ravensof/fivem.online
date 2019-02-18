package online.fivem.client.modules.nui_event_exchanger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.SEvent
import kotlin.coroutines.CoroutineContext

object NuiEvent : SEvent(), CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	fun emitUnsafe(data: Any) = launch {
		NuiEventExchangerModule.unsafeChannel.send(data)
	}

	override suspend fun emit(data: Any) {
		NuiEventExchangerModule.channel.send(data)
	}

	fun emitAsync(data: Any) = launch {
		emit(data)
	}

	suspend fun handle(data: Any) {
		super.emit(data)
	}
}