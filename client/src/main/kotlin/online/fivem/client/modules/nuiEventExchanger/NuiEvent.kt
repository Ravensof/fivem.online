package online.fivem.client.modules.nuiEventExchanger

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent

object NuiEvent : UEvent(Job()) {

	override val printType = "nui"

	override fun emit(data: Any): Job {
		return launch {
			NuiEventExchangerModule.channel.send(data)
		}
	}

	fun handle(data: Any) {
		super.emit(data)
	}
}