package online.fivem.client.modules.basics

import online.fivem.client.common.AbstractClientModule
import online.fivem.client.entities.Date
import online.fivem.common.common.VDate

class DateTimeModule : AbstractClientModule() {

	val date = VDate()

	override fun onInit() {
		moduleLoader.on<TickExecutorModule> { start(it) }
	}

	private fun start(tickExecutor: TickExecutorModule) {
		tickExecutor.add {
			Date.setDate(date.day, date.month + 1, date.year)
			Date.setTime(date.hour, date.minute, date.second)
		}
	}
}