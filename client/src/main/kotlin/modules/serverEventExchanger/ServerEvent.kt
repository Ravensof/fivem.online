package online.fivem.client.modules.serverEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent
import kotlin.reflect.KClass

object ServerEvent : UEvent() {

	override fun emit(kClass: KClass<out Any>, data: Any): Job {
		return GlobalScope.launch {
			ServerEventExchanger.channel.send(data)
		}
	}

	fun handle(data: Any) {
		super.emit(data::class, data)
	}
}