package server

import fivem.common.Exports
import fivem.common.on
import server.common.Server
import server.extensions.onNet
import server.modules.player.PlayerModule
import server.modules.session.SessionModule
import server.modules.test.TestModule
import server.modules.web.WebModule
import server.structs.PlayerSrc
import universal.common.Console
import universal.common.Event
import universal.events.ConsoleLogEvent
import universal.r.MODULE_FOLDER_NAME
import universal.r.NativeEvents
import universal.r.NativeEvents.Server.PLAYER_DROPPED


fun start() {

	try {

//		on("rconCommand", {command: String, commandArguments: Array<String>->})
		Exports.on(PLAYER_DROPPED) { source: Int, reason: String ->
			Console.log("$source $reason")
		}

		Event.on { event: ConsoleLogEvent ->
			Console.log("server received message \"${event.message}\"")
		}

		Event.onNet { player: PlayerSrc, event: ConsoleLogEvent ->
			Console.log("server received message \"${event.message}\" from ${player.value}")
		}

		Event.onNet { playerSrc: PlayerSrc, str: String ->

			println("playerSrc: $playerSrc")
			println("string: $str")

			Console.debug(Server.getPlayerIdentifiers(playerSrc))
		}

		SessionModule.getInstance()
		PlayerModule.getInstance()
		WebModule.getInstance()
		TestModule.getInstance()

//	setTimeout {
//		MySQL.query("SELECT `id`,`steam`,`license`,`ip` FROM player_identifiers") { data: Array<PlayerIdentifiers> ->
//			data.forEach {
//				Console.log(it)
//			}
//		}
//	}

		Console.info("server started")
	} catch (exception: Exception) {
		Console.error(exception.message)
	}
}

fun main(args: Array<String>) {
	Console.debug("hello server")

//	MODULE_FOLDER_NAME = FiveM.getCurrentResourceName()

	on(NativeEvents.Server.RESOURCE_START) { resourceName: String ->
		if (resourceName == MODULE_FOLDER_NAME) {
			start()
		}
	}
}