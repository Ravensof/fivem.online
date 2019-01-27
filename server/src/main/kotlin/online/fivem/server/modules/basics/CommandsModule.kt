package online.fivem.server.modules.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule

class CommandsModule : AbstractModule() {

	override fun onStart(): Job? {
		GlobalScope.launch {
			for (command in executionQueue) {
				CommandEvent.handle(command)
			}
		}

		return super.onStart()
	}

	override fun onStop(): Job? {
		executionQueue.close()

		return super.onStop()
	}

	class Command(
		val playerSrc: Int,
		val command: String,
		val args: Array<String>,
		val raw: String
	)

	companion object {
		val executionQueue = Channel<Command>(32)
	}
}