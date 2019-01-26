package online.fivem.client.modules.serverEventExchanger

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent

object ServerEvent : UEvent(Job()) {

	override fun emit(data: Any): Job {
		return launch {
			ServerEventExchangerModule.channel.send(data)
		}
	}

	fun handle(data: Any) {
		super.emit(data)
	}
}