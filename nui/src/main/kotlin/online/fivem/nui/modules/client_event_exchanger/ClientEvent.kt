package online.fivem.nui.modules.client_event_exchanger

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent

object ClientEvent : UEvent(Job()) {

	override val printType = "nui"

	override fun emit(data: Any) {
		launch {
			ClientEventExchangerModule.channel.send(data)
		}
	}

	fun handle(data: Any) {
		super.emit(data)
	}
}