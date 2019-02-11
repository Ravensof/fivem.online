package online.fivem.client.modules.server_event_exchanger

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent

object ServerEvent : UEvent(Job()) {

	override fun emit(data: Any) {
		launch {
			ServerEventExchangerModule.channel.send(data)
		}
	}

	fun handle(data: Any) {
		super.emit(data)
	}
}