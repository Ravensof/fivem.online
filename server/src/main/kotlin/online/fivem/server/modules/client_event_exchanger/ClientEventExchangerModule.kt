package online.fivem.server.modules.client_event_exchanger

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.common.GlobalConfig
import online.fivem.common.Serializer
import online.fivem.common.common.Console
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.net.AcceptEvent
import online.fivem.common.events.net.EstablishConnectionEvent
import online.fivem.common.events.net.ImReadyEvent
import online.fivem.common.events.net.StopResourceEvent
import online.fivem.common.extensions.deserialize
import online.fivem.common.extensions.forEach
import online.fivem.common.extensions.onNull
import online.fivem.common.extensions.serializeToPacket
import online.fivem.common.gtav.NativeEvents
import online.fivem.common.other.ClientsNetPacket
import online.fivem.common.other.Serializable
import online.fivem.common.other.SerializerInterface
import online.fivem.common.other.ServersNetPacket
import online.fivem.server.Strings
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.gtav.Exports
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.basics.SessionModule
import kotlin.collections.set
import kotlin.random.Random

private typealias Data = Any

class ClientEventExchangerModule : AbstractServerModule() {

	private val playersSendChannels = createChannels<Serializable>()
	private val playersReceiveChannels = createChannels<IncomingPacket>()

	private val clients = mutableMapOf<Int, Double>()

	private val sessionModule by moduleLoader.delegate<SessionModule>()

	init {
		Exports.on(NativeEvents.Server.PLAYER_DROPPED) { playerId: Int, _: String -> onPlayerDropped(playerId) }

		Natives.onNet(GlobalConfig.NET_EVENT_NAME, ::onNetEvent)

		Natives.onNet(GlobalConfig.NET_EVENT_ESTABLISHING_NAME, ::onEstablishingConnection)

	}

	@ExperimentalCoroutinesApi
	override fun onStart(): Job? {
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
					val playerSrc = packet.playerSrc

					sessionModule.getPlayer(playerSrc.value)?.let {
						ClientEvent.handle(it, packet.data)
					}.onNull {
						ClientEvent.handleGuest(playerSrc, packet.data)
					}
				}
			}
		}

		launch {
			for (packet in channel) {
				packet.playerSrc?.value?.let { playerSrc ->

					val channel = playersSendChannels[playerSrc]

					channel.send(packet.data)

				}.onNull {
					clients.forEach {
						val channel = playersSendChannels[it.key]

						channel.send(packet.data)
					}
				}
			}
		}

		return super.onStart()
	}

	override fun onStop() = launch {

		val eventId = Random.nextInt()

		launch {
			clients.forEach {
				launch {
					val channel = ClientEvent.openSubscription(AcceptEvent::class)
					emit(it.key, StopResourceEvent(eventId))

					try {
						withTimeout(PLAYER_SAVE_TIMEOUT) {
							channel.forEach {
								if (it.data.eventId == eventId) {
									channel.cancel()
								}
							}
						}
					} catch (e: TimeoutCancellationException) {
						Console.warn("player save for id=${it.key} is timed out")
					} finally {
						channel.cancel()
					}
				}
			}
		}.join()
	}

	private fun onEstablishingConnection(playerSrc: PlayerSrc, netPacket: Any) {
		try {
//				val netPacket =
			Serializer.deserialize<ImReadyEvent>(netPacket.toString())
			onClientReady(playerSrc)
		} catch (e: SerializerInterface.DeserializationException) {
			Natives.dropPlayer(
				playerSrc,
				Strings.CLIENT_WRONG_PACKET_FORMAT
			)
		}
	}

	private fun onNetEvent(playerSrc: PlayerSrc, rawPacket: Any) {
		try {
			val packet = rawPacket.unsafeCast<ClientsNetPacket>()

			val data = Serializer.deserialize(packet)

			if (clients[playerSrc.value] != packet.key) throw Exception(Strings.CLIENT_WRONG_PACKET_FORMAT)

//			if (netPacket.playersCount == 1 && Natives.getPlayers().count() > 1) return@onNet Natives.dropPlayer(
//				playerSrc,
//				Strings.CLIENT_SINGLE_SESSION
//			)

			val channel = playersReceiveChannels[playerSrc.value]

			launch {
				withTimeoutOrNull(5_000) {
					channel.send(IncomingPacket(playerSrc, data))
					true
				}.onNull {
					Console.warn(
						"ClientEventExchanger: processing packet for player ${playerSrc.value} is timed out (${data::class.simpleName} ${JSON.stringify(
							data
						)})"
					)

//					if (ServerConfig.KICK_FOR_PACKET_OVERFLOW) throw Exception(Strings.CLIENT_PACKETS_OVERFLOW)
				}
			}
		} catch (exception: Throwable) {
			return Natives.dropPlayer(
				playerSrc,
				exception.message.toString()
			)
		}
	}

	private fun onPlayerDropped(playerId: Int) {
		clients.remove(playerId)
	}

	private fun onClientReady(playerSrc: PlayerSrc) {
		if (clients.containsKey(playerSrc.value)) return Natives.dropPlayer(
			playerSrc,
			Strings.CLIENT_ALREADY_CONNECTED
		)

		val key = Random.nextDouble()

		clients[playerSrc.value] = key

		emit(playerSrc.value, EstablishConnectionEvent(key))
	}

	private fun emit(playerSrc: Int, data: Serializable) {
		val packet = ServersNetPacket(Serializer.serializeToPacket(data))

		Natives.emitNet(
			eventName = GlobalConfig.NET_EVENT_NAME,
			playerSrc = playerSrc,
			data = packet
		)
	}

	private fun <T> createChannels(): List<Channel<T>> {
		val list = mutableListOf<Channel<T>>()

		for (i in 0 until GlobalConfig.MAX_PLAYERS) {
			list.add(Channel(PLAYERS_CHANNEL_SIZE))
		}

		return list
	}

	class IncomingPacket(
		val playerSrc: PlayerSrc,
		val data: Data
	)

	class SendingPacket(
		val playerSrc: PlayerSrc? = null,
		val data: Serializable
	)

	companion object {
		private const val PLAYER_SAVE_TIMEOUT: Long = 15_000
		private const val PLAYERS_CHANNEL_SIZE = 128 * GlobalConfig.MAX_PLAYERS

		val channel = Channel<SendingPacket>(PLAYERS_CHANNEL_SIZE)
	}
}