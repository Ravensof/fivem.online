package online.fivem.server.modules.clientEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Serializer
import online.fivem.common.entities.NetPacket
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.EstablishConnection
import online.fivem.common.events.ImReady
import online.fivem.common.extensions.onNull
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.Strings
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import kotlin.random.Random

class ClientEventExchangerModule : AbstractModule() {

	private val playersList = mutableMapOf<PlayerSrc, Long>()

	override fun start(): Job? {

		Exports.on(NativeEvents.Server.PLAYER_DROPPED) { playerId: Int, _: String -> onPlayerDropped(playerId) }

		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { playerSrc: PlayerSrc, netPacket: Any ->
			@Suppress("NAME_SHADOWING") val netPacket = Serializer.unpack<NetPacket>(netPacket)

			if (playersList[playerSrc] != netPacket.key) return@onNet Natives.dropPlayer(
				playerSrc,
				Strings.CLIENT_WRONG_PACKET_FORMAT
			)

			ClientEvent.handle(playerSrc, netPacket.data)
		}

		Natives.onNet(GlobalConfig.NET_EVENT_ESTABLISHING_NAME) { playerSrc: PlayerSrc, netPacket: Any ->
			@Suppress("NAME_SHADOWING") val netPacket = Serializer.unpack<NetPacket>(netPacket)

			if (netPacket.data !is ImReady) return@onNet Natives.dropPlayer(
				playerSrc,
				Strings.CLIENT_WRONG_PACKET_FORMAT
			)

			onClientReady(playerSrc)
		}

		GlobalScope.launch {
			//todo из-за одного клиента может остановиться передача данных. выделить на каждого по каналу?
			for (packet in channel) {
				packet.playerSrc?.value?.let { playerSrc ->
					emit(playerSrc, packet.data)
				}.onNull {
					Natives.getPlayers().forEach {
						emit(it.value, packet.data)
					}
				}
			}
		}

		return super.start()
	}

	private fun onPlayerDropped(playerId: Int) {
		playersList.forEach {
			if (it.key.value == playerId) {
				playersList.remove(it.key)
				return@forEach
			}
		}
	}

	private fun onClientReady(playerSrc: PlayerSrc) {
		if (playersList.containsKey(playerSrc)) return Natives.dropPlayer(playerSrc, Strings.CLIENT_ALREADY_CONNECTED)

		val key = Random.nextLong()

		playersList[playerSrc] = key

		emit(playerSrc.value, EstablishConnection(key))
	}

	private fun emit(playerSrc: Int, data: Any) {
		Natives.emitNet(
			eventName = GlobalConfig.NET_EVENT_NAME,
			playerSrc = playerSrc,
			data = Serializer.prepare(
				NetPacket(data = data)
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