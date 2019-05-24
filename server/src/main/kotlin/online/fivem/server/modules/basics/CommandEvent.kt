package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.fivem.common.common.createJob
import online.fivem.server.entities.Player
import online.fivem.server.gtav.Natives.registerCommand
import kotlin.coroutines.CoroutineContext

private typealias Handler = suspend (Player?, Array<String>, String) -> Unit

object CommandEvent : CoroutineScope {
	override val coroutineContext: CoroutineContext = createJob()

	private val handlers = mutableMapOf<String, Handler>()

	fun on(command: String, callback: Handler) {
		handlers[command] = callback

		registerCommand(command, false) { playerSrc, args, raw ->
			launch {
				CommandsModule.executionQueue.send(CommandsModule.RawCommand(playerSrc, command, args, raw))
			}
		}
	}

	suspend fun handle(command: CommandsModule.Command) {
		handlers[command.command]?.invoke(command.player, command.args, command.raw)
	}
}