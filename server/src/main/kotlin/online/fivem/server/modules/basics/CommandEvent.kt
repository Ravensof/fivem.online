package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.fivem.Natives
import online.fivem.common.common.createSupervisorJob
import online.fivem.server.entities.Player
import kotlin.coroutines.CoroutineContext

private typealias Handler = suspend (Player?, Array<String>, String) -> Unit

object CommandEvent : CoroutineScope {
	override val coroutineContext: CoroutineContext = createSupervisorJob()

	private val handlers = mutableMapOf<String, Handler>()

	fun on(command: String, callback: Handler) {
		handlers[command] = callback

		launch {
			Natives.registerCommand(command, false) { playerSrc, args, raw ->
				this@CommandEvent.launch {
					CommandsModule.executionQueue.send(CommandsModule.RawCommand(playerSrc, command, args, raw))
				}
			}
		}
	}

	suspend fun handle(command: CommandsModule.Command) {
		handlers[command.command]?.invoke(command.player, command.args, command.raw)
	}
}