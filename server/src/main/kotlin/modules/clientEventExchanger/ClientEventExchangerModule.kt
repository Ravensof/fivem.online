package online.fivem.server.modules.clientEventExchanger

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Serializer
import online.fivem.common.entities.ClientsNetPacket
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.entities.ServersNetPacket
import online.fivem.common.events.EstablishConnectionEvent
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.extensions.onNull
import online.fivem.common.gtav.NativeEvents
import online.fivem.server.ServerConfig
import online.fivem.server.Strings
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

private typealias Data = Any

class ClientEventExchangerModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = Job()

	private val playersSendChannels = createChannels<Data>()
	private val playersReceiveChannels = createChannels<Packet<*>>()

	private val playersList = mutableMapOf<Int, Double>()

	override fun init() {
		Exports.on(NativeEvents.Server.PLAYER_DROPPED) { playerId: Int, _: String -> onPlayerDropped(playerId) }

		Natives.onNet(GlobalConfig.NET_EVENT_NAME) { playerSrc: PlayerSrc, netPacket: Any ->
			@Suppress("NAME_SHADOWING")
			val netPacket = Serializer.unpack<ClientsNetPacket>(netPacket)

			if (playersList[playerSrc.value] != netPacket.key) return@onNet Natives.dropPlayer(
				playerSrc,
				Strings.CLIENT_WRONG_PACKET_FORMAT
			)

			if (netPacket.playersCount == 1 && Natives.getPlayers().count() > 1) return@onNet Natives.dropPlayer(
				playerSrc,
				Strings.CLIENT_SINGLE_SESSION
			)

			launch {
				val channel = playersReceiveChannels[playerSrc.value]
				if (channel.isFull) {
					Console.warn("ClientEventExchanger: receive channel for player ${playerSrc.value} is full")

					if (ServerConfig.KICK_FOR_PACKET_OVERFLOW) return@launch Natives.dropPlayer(
						playerSrc,
						Strings.CLIENT_PACKETS_OVERFLOW
					)
				}
				channel.send(Packet<Any>(playerSrc, netPacket.data))
			}
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

	@ExperimentalCoroutinesApi
	override fun start(): Job? {
		playersSendChannels.forEachIndexed { playerSrc, channel ->
			launch {
				for (data in channel) {
					emit(playerSrc, data)
				}
			}
		}

		playersReceiveChannels.forEach { channel ->
			launch {
				for (packet in channel) {
					ClientEvent.handle(packet.playerSrc!!, packet.data)
				}
			}
		}

		launch {
			for (packet in channel) {
				packet.playerSrc?.value?.let { playerSrc ->

					val channel = playersSendChannels[playerSrc]

					if (channel.isFull) {
						Console.warn("ClientEventExchanger: emit channel for player $playerSrc is full")
					}

					channel.send(packet.data)

				}.onNull {
					playersList.forEach {
						val channel = playersSendChannels[it.key]

						if (channel.isFull) {
							Console.warn("ClientEventExchanger: emit channel for player ${it.key} is full")
						}

						channel.send(packet.data)
					}
				}
			}
		}

		return super.start()
	}

	override fun stop(): Job? {
		cancel()

		return super.stop()
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

	private fun <T> createChannels(): List<Channel<T>> {
		val list = mutableListOf<Channel<T>>()

		for (i in 0 until GlobalConfig.MAX_PLAYERS) {
			list.add(Channel(PLAYERS_CHANNEL_SIZE))
		}

		return list
	}

	class Packet<Response>(
		val playerSrc: PlayerSrc? = null,
		val data: Data,
		val timeout: Long = 0,
		val callback: ((PlayerSrc, Response) -> Unit)? = null
	)

	companion object {
		private const val PLAYERS_CHANNEL_SIZE = 128

		val channel = Channel<Packet<*>>(PLAYERS_CHANNEL_SIZE)
	}
}