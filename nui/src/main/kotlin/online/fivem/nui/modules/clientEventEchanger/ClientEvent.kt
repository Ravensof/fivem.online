package online.fivem.nui.modules.clientEventEchanger

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.Serializer
import online.fivem.common.common.UEvent

object ClientEvent : UEvent(Job()) {

	override val printType = "nui"

	override fun emit(data: Any): Job {
		return launch {
			ClientEventExchangerModule.channel.send(
				Serializer.serialize(data)
			)
		}
	}

	fun handle(data: Any): Job {
		return super.emit(data)
	}
}