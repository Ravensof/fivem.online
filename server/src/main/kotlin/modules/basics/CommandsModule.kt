package online.fivem.server.modules.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule

class CommandsModule : AbstractModule() {

	override fun start(): Job? {
		GlobalScope.launch {
			for (command in executionQueue) {
				CommandEvent.handle(command)
			}
		}

		return super.start()
	}

	override fun stop(): Job? {
		executionQueue.close()

		return super.stop()
	}

	class Command(
		val playerSrc: Int,
		val command: String,
		val args: Array<String>,
		val raw: String
	)

	companion object {
		val executionQueue = Channel<Command>()
	}
}