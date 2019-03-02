package online.fivem.server.modules.basics

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.entities.Player
import kotlin.coroutines.CoroutineContext

class CommandsModule(override val coroutineContext: CoroutineContext) : AbstractServerModule() {

	private val sessionModule by moduleLoader.delegate<SessionModule>()

	override fun onStart(): Job? {
		launch {
			for (command in executionQueue) {
				val player =
					if (command.playerSrc == 0) null else sessionModule.getPlayer(command.playerSrc) ?: continue

				CommandEvent.handle(
					Command(
						player = player,
						command = command.command,
						args = command.args,
						raw = command.raw
					)
				)
			}
		}

		return super.onStart()
	}

	override fun onStop(): Job? {
		executionQueue.close()

		return super.onStop()
	}

	class RawCommand(
		val playerSrc: Int,
		val command: String,
		val args: Array<String>,
		val raw: String
	)

	class Command(
		val player: Player?,
		val command: String,
		val args: Array<String>,
		val raw: String
	)

	companion object {
		val executionQueue = Channel<RawCommand>(32)
	}
}