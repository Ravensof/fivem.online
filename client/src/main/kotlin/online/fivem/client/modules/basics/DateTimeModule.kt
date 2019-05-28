package online.fivem.client.modules.basics

import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.entities.Date
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.common.common.VDate
import online.fivem.common.events.net.ServerSideSynchronizationEvent

class DateTimeModule : AbstractClientModule() {

	val date = VDate()

	init {
		ServerEvent.on<ServerSideSynchronizationEvent> {
			date.serverRealTime = it.serverTime
		}
	}

	override fun onStart() = launch {
		val tickExecutor = moduleLoader.getModule(TickExecutorModule::class)

		tickExecutor.add {
			Date.setDate(date.day, date.month + 1, date.year)
			Date.setTime(date.hour, date.minute, date.second)
		}
	}
}