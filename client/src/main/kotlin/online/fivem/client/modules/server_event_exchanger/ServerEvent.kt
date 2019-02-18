package online.fivem.client.modules.server_event_exchanger

import online.fivem.common.common.SEvent

object ServerEvent : SEvent() {

	override suspend fun emit(data: Any) {
		ServerEventExchangerModule.channel.send(data)
	}

	suspend fun handle(data: Any) {
		super.emit(data)
	}
}