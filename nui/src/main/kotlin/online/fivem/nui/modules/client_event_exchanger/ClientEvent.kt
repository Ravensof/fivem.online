package online.fivem.nui.modules.client_event_exchanger

import online.fivem.common.common.Event

object ClientEvent : Event() {

	override suspend fun emit(data: Any) {
		ClientEventExchangerModule.channel.send(data)
	}

	suspend fun handle(data: Any) {
		super.emit(data)
	}
}