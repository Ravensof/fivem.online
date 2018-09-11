package server

import server.extensions.onNet
import server.structs.PlayerSrc
import shared.Console
import shared.Event
import shared.Exports
import shared.events.ConsoleLogEvent
import shared.on
import shared.r.MODULE_FOLDER_NAME
import shared.struct.tables.PlayerIdentifiers

fun start() {

//		on("rconCommand", {command: String, commandArguments: Array<String>->})
	Exports.on("playerDropped") { source: Int, reason: String ->
		Console.log("$source $reason")
	}

	Exports.on("playerConnecting") { source: Int, playerName: String, setKickReason: (reason: String) -> Unit ->
		//		MySQL.execute("INSERT")
		//		setKickReason("test for $playerName with id = $source and "+Server.getPlayerEndpoint(PlayerSrc(source)))
//		Server.cancelEvent()
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

		Console.info(Server.getPlayerIdentifiers(playerSrc))
	}



	MySQL.query("SELECT `id`,`steam`,`license`,`ip` FROM player_identifiers") { data: Array<PlayerIdentifiers> ->
		//			data.forEach {
//				console.log(it.id.toString()+" " +it.license)
//			}
	}






	Console.info("server started")
}

fun main(args: Array<String>) {
	Console.debug("hello server")

	on("onServerResourceStart") { resourceName: String ->
		if (resourceName == MODULE_FOLDER_NAME) {
			start()
		}
	}
}