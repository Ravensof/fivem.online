package online.fivem.client.modules.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.modules.server_event_exchanger.ServerEvent
import online.fivem.client.modules.server_event_exchanger.ServerEventExchangerModule
import online.fivem.common.common.Console
import online.fivem.common.common.ExceptionsStorage
import online.fivem.common.events.net.ErrorReportEvent
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.stackTrace

class ErrorReporterModule : AbstractClientModule(), ExceptionsStorage.Listener {

	override suspend fun onInit() {
		launch {
			moduleLoader.getModule(ServerEventExchangerModule::class)

			ExceptionsStorage.listener = this@ErrorReporterModule

			launch {
				channel.forEach {
					ServerEvent.emit(ErrorReportEvent(it))
				}
			}
		}
	}

	override fun onThrowable(throwable: Throwable) {
		handleError(throwable)
	}

	companion object {
		private val channel = Channel<String>(32)

		fun handleError(throwable: Throwable) {
			val message = throwable.stackTrace()

			GlobalScope.launch {
				channel.send(message)
			}
			Console.error(message)
		}
	}
}