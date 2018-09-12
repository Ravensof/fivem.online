package server

import server.extensions.onNet
import server.structs.PlayerSrc
import shared.*
import shared.events.ConsoleLogEvent
import shared.r.MODULE_FOLDER_NAME
import shared.r.NativeEvents
import shared.struct.tables.PlayerIdentifiers


fun start() {

//		on("rconCommand", {command: String, commandArguments: Array<String>->})
	Exports.on("playerDropped") { source: Int, reason: String ->
		Console.log("$source $reason")
	}

	Exports.on("playerConnecting") { source: Int, playerName: String, setKickReason: (reason: String) -> Unit ->

		setTimeout {
			val playerSrc = PlayerSrc(source)
			val playerIdentifiers = Server.getPlayerIdentifiers(playerSrc)

			MySQL.execute("INSERT INTO ${PlayerIdentifiers.TABLE_NAME} " +
					"SET ${PlayerIdentifiers.FIELD_LAST_NAME}=${MySQL.filter(playerName)}, " +
					"${PlayerIdentifiers.FIELD_STEAM}=${MySQL.filter(playerIdentifiers.steam.orEmpty())}, " +
					"${PlayerIdentifiers.FIELD_LICENSE}=${MySQL.filter(playerIdentifiers.license.orEmpty())}, " +
					"${PlayerIdentifiers.FIELD_IP}=${MySQL.filter(playerIdentifiers.ip.orEmpty())} " +
					"ON DUPLICATE KEY UPDATE " +
					"${PlayerIdentifiers.FIELD_LAST_NAME}=${MySQL.filter(playerName)}")
		}

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

		Console.debug(Server.getPlayerIdentifiers(playerSrc))
	}


	MySQL.query("SELECT `id`,`steam`,`license`,`ip` FROM player_identifiers") { data: Array<PlayerIdentifiers> ->
		data.forEach {
			Console.log(it)
		}
	}

	Console.log(Base64.fromBase64(Base64.toBase64("test")))

	Console.info("server started")
}

fun main(args: Array<String>) {
	Console.debug("hello server")

	on(NativeEvents.Server.RESOURCE_START) { resourceName: String ->
		if (resourceName == MODULE_FOLDER_NAME) {
			start()
		}
	}
}