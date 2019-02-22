package online.fivem.nui.modules.client_event_exchanger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.common.Event
import kotlin.coroutines.CoroutineContext

object ClientEvent : Event(), CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	override suspend fun emit(data: Any) {
		ClientEventExchangerModule.channel.send(data)
	}

	suspend fun handle(data: Any) {
		super.emit(data)
	}
}