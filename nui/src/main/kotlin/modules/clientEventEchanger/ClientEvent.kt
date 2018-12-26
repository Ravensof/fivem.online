package online.fivem.nui.modules.clientEventEchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.Serializer
import online.fivem.common.common.UEvent
import kotlin.reflect.KClass

object ClientEvent : UEvent() {

	override val printType = "nui"

	fun emit(data: Any): Job {
		return emit(data::class, data)
	}

	override fun emit(kClass: KClass<out Any>, data: Any): Job {
		return GlobalScope.launch {
			ClientEventExchanger.channel.send(
				Serializer.serialize(data)
			)
		}
	}

	fun handle(data: Any): Job {
		return super.emit(data::class, data)
	}
}