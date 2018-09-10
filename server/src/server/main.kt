package server

import server.extensions.onNet
import server.structs.PlayerSrc
import shared.Console
import shared.Event
import shared.events.ConsoleLogEvent
import shared.on
import shared.r.MODULE_FOLDER_NAME


fun start() {

//	MySQL.get("SELECT * FROM fivemuser")


	Event.on { event: ConsoleLogEvent ->
		console.log("server received message \"${event.message}\"")
	}
	Event.onNet { player: PlayerSrc, event: ConsoleLogEvent ->
		console.log("server received message \"${event.message}\" from ${player.value}")
	}

	Event.onNet { playerSrc: PlayerSrc, str: String ->

		println("playerSrc: $playerSrc")
		println("string: $str")
	}


	Console.info("server started")
}

fun main(args: Array<String>) {
	Console.debug("hello server")

	on("playerDropped", PlayersManager::onClientDropped)

	on("onServerResourceStart") { resourceName: String ->
		if (resourceName == MODULE_FOLDER_NAME) {
			start()
		}
	}
//		on("rconCommand", {command: String, commandArguments: Array<String>->})
//		on("playerConnecting", { playerName: String, setKickReason: (reason: String)->Unit , tempPlayer: Any->})


}