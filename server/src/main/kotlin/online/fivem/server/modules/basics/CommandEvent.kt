package online.fivem.server.modules.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import online.fivem.server.gtav.Natives.registerCommand

private typealias Handler = (Int, Array<String>, String) -> Unit

object CommandEvent {

	private val handlers = mutableMapOf<String, Handler>()

	fun on(command: String, callback: Handler) {
		handlers[command] = callback

		registerCommand(command, false) { playerSrc, args, raw ->
			handle(playerSrc, command, args, raw)
		}
	}

	fun handle(command: CommandsModule.Command) {
		handlers[command.command]?.invoke(command.playerSrc, command.args, command.raw)
	}

	private fun handle(playerSrc: Int, command: String, args: Array<String>, raw: String) {
		GlobalScope.launch {
			CommandsModule.executionQueue.send(CommandsModule.Command(playerSrc, command, args, raw))
		}
	}
}