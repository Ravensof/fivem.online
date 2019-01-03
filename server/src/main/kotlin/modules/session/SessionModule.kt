package online.fivem.server.modules.session

import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives

class SessionModule : AbstractModule() {
	override fun start(): Job? {
		Exports.on(NativeEvents.Server.PLAYER_CONNECTING, ::onClientConnecting)

		Exports.on(NativeEvents.Server.PLAYER_DROPPED, ::onPlayerDropped)

		return super.start()
	}

	private fun onPlayerDropped(playerId: Int, reason: String) {
//		playersIds.remove(playerId)
		Console.log("$playerId $reason")
	}

	private fun onClientConnecting(source: Int, playerName: String, setKickReason: (reason: String) -> Unit) {
		Console.log("onClientConnecting $source")
//		setTimeout {
//			val playerSrc = PlayerSrc(source)
//
//			saveConnectionData(playerSrc, playerName)
//		}
//DropPlayer(playerSrc, Strings.SERVER_REGISTER_PLAYER_ERROR)
//		setKickReason("test for $playerName with id = $source and "+Server.getPlayerEndpoint(PlayerSrc(source)))
	}
}