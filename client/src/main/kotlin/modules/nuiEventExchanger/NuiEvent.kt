package online.fivem.client.modules.nuiEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent
import kotlin.reflect.KClass

object NuiEvent : UEvent() {

	override val printType = "nui"

	fun emit(data: Any) {
		emit(data::class, data)
	}

	override fun emit(kClass: KClass<out Any>, data: Any): Job {
		return GlobalScope.launch {
			NuiEventExchanger.channel.send(data)
		}
	}

	fun handle(data: Any) {
		super.emit(data::class, data)
	}
}