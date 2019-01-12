package online.fivem.server.modules.clientEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Serializer
import online.fivem.common.entities.ClientsNetPacket
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.entities.ServersNetPacket
import online.fivem.common.events.EstablishConnectionEvent
import online.fivem.common.events.ImReadyEvent
import online.fivem.common.extensions.onNull
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.Strings
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.session.SessionModule
import kotlin.random.Random

class ClientEventExchangerModule : AbstractModule() {

	private val playersList = mutableMapOf<Int, Double>()
	private val sessionModule by moduleLoader.onReady<SessionModule>()

	override fun init() {
		Exports.on(NativeEvents.Server.PLAYER_DROPPED) { playerId: Int, _: String -> onPlayerDropped(playerId) }

		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { playerSrc: PlayerSrc, netPacket: Any ->
			@Suppress("NAME_SHADOWING")
			val netPacket = Serializer.unpack<ClientsNetPacket>(netPacket)

			if (playersList[playerSrc.value] != netPacket.key) return@onNet Natives.dropPlayer(
				playerSrc,
				Strings.CLIENT_WRONG_PACKET_FORMAT
			)

			if (netPacket.playersCount == 1 && Natives.countPlayersOnline() > 1) return@onNet Natives.dropPlayer(
				playerSrc,
				Strings.CLIENT_SINGLE_SESSION
			)

			ClientEvent.handle(playerSrc, netPacket.data)
		}

		Natives.onNet(GlobalConfig.NET_EVENT_ESTABLISHING_NAME) { playerSrc: PlayerSrc, netPacket: Any ->
			try {
				@Suppress("NAME_SHADOWING")
				val netPacket = Serializer.unpack<ImReadyEvent>(netPacket)
				onClientReady(playerSrc)
			} catch (e: Serializer.DeserializationException) {
				Natives.dropPlayer(
					playerSrc,
					Strings.CLIENT_WRONG_PACKET_FORMAT
				)
			}
		}
	}

	override fun start(): Job? {

		GlobalScope.launch {
			//todo из-за одного клиента может остановиться передача данных. выделить на каждого по каналу?
			for (packet in channel) {
				packet.playerSrc?.value?.let { playerSrc ->
					emit(playerSrc, packet.data)
				}.onNull {
					sessionModule.getConnectedPlayers().forEach {
						emit(it.value, packet.data)
					}
				}
			}
		}

		return super.start()
	}

	fun getConnectedClients(): List<PlayerSrc> {
		return playersList.map { PlayerSrc(it.key) }
	}

	private fun onPlayerDropped(playerId: Int) {
		playersList.remove(playerId)
	}

	private fun onClientReady(playerSrc: PlayerSrc) {
		if (playersList.containsKey(playerSrc.value)) return Natives.dropPlayer(
			playerSrc,
			Strings.CLIENT_ALREADY_CONNECTED
		)

		val key = Random.nextDouble()

		playersList[playerSrc.value] = key
		emit(playerSrc.value, EstablishConnectionEvent(key))
	}

	private fun emit(playerSrc: Int, data: Any) {
		Natives.emitNet(
			eventName = GlobalConfig.NET_EVENT_NAME,
			playerSrc = playerSrc,
			data = Serializer.serialize(
				ServersNetPacket(data = data)
			)
		)
	}

	class Packet(
		val playerSrc: PlayerSrc? = null,
		val data: Any
	)

	companion object {
		val channel = Channel<Packet>()
	}
}