package online.fivem.server.modules.session

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.ImReadyEvent
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.Strings
import online.fivem.server.common.MySQL
import online.fivem.server.entities.Player
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.clientEventExchanger.ClientEvent
import online.fivem.server.modules.clientEventExchanger.ClientEventExchangerModule
import online.fivem.server.mysqlTables.BlackListTable

class SessionModule : AbstractModule() {

	private val mySQL = MySQL.instance
	private val players = mutableMapOf<PlayerSrc, Player>()

	private val clientEventExchangerModule by moduleLoader.onReady<ClientEventExchangerModule>()

	override fun init() {
		Exports.on(NativeEvents.Server.PLAYER_CONNECTING, ::onClientConnecting)
		Exports.on(NativeEvents.Server.PLAYER_DROPPED, ::onPlayerDropped)

		ClientEvent.on<ImReadyEvent> { playerSrc, _ -> onClientReady(playerSrc) }
	}

	override fun start(): Job? {
		clientEventExchangerModule.getConnectedClients().forEach {
			onClientReady(it)
		}

		return super.start()
	}

	fun getConnectedPlayers(): List<PlayerSrc> {
		return Natives.getPlayers()
	}

	private fun onClientReady(playerSrc: PlayerSrc) {
		Console.debug("onClientReady ${playerSrc.value}")
		val identifiers = Natives.getPlayerIdentifiers(playerSrc)

		players[playerSrc] = Player(
			playerSrc = playerSrc,
			name = identifiers.name.orEmpty()
		)
	}

	private fun onPlayerDropped(playerId: Int, reason: String) {

		players.forEach {
			if (it.key.value == playerId) {
				val player = it.value
				players.remove(it.key)

				return Console.log("disconnected ${player.name}: $reason")
			}
		}

		Console.log("disconnected $playerId: $reason")
	}

	private fun onClientConnecting(source: Int, playerName: String, setKickReason: (reason: String) -> Unit) {

		val identifiers = Natives.getPlayerIdentifiers(source)

		val query = mySQL.query<BlackListTable>(
			"SELECT reason " +
					"FROM `black_list` " +
					"WHERE " +
					"ip=${MySQL.filter(identifiers.ip.orEmpty())} " +
					"OR steam=${MySQL.filter(identifiers.steam.orEmpty())} " +
					"OR license=${MySQL.filter(identifiers.license.orEmpty())} " +
					"LIMIT 1"
		)

		Console.log("connecting $playerName ${identifiers.ip} ${identifiers.license} ${identifiers.steam}")

		GlobalScope.launch {
			val result = query.await()

			if (result.isNotEmpty()) {
				val reason = Strings.YOU_ARE_BANNED_FROM_THIS_SERVER.replace("%s", result.first().reason.orEmpty())
				Natives.dropPlayer(source, reason)
			}
		}
	}
}