package online.fivem.client.modules.serverEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent

object ServerEvent : UEvent() {

	override fun emit(data: Any): Job {
		return GlobalScope.launch {
			ServerEventExchangerModule.channel.send(data)
		}
	}

	fun handle(data: Any) {
		super.emit(data)
	}
}