package online.fivem.server.modules.client_event_exchanger

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.other.Serializable
import online.fivem.server.entities.Player
import kotlin.reflect.KClass

object ClientEvent {

	private const val CHANNEL_CAPACITY = 1

	private val guestChannels =
		mutableMapOf<KClass<*>, BroadcastChannel<GuestParams<*>>>()
	private val playerChannels =
		mutableMapOf<KClass<*>, BroadcastChannel<PlayerParams<*>>>()


	fun <T : Any> openSubscription(kClass: KClass<T>): ReceiveChannel<PlayerParams<T>> {
		return getPlayerChannel(kClass).openSubscription()
	}

	fun <T : Any> openGuestSubscription(kClass: KClass<T>): ReceiveChannel<GuestParams<T>> {
		return getGuestChannel(kClass).openSubscription()
	}

	suspend fun emit(
		data: Serializable,
		playerSrc: PlayerSrc
	) {
		ClientEventExchangerModule.channel.send(
			ClientEventExchangerModule.SendingPacket(
				playerSrc = playerSrc,
				data = data
			)
		)
	}

	suspend fun emitAll(
		data: Serializable
	) {
		ClientEventExchangerModule.channel.send(
			ClientEventExchangerModule.SendingPacket(
				playerSrc = null,
				data = data
			)
		)
	}

	suspend fun emit(
		data: Serializable,
		player: Player
	) = emit(data, player.playerSrc)

	suspend fun <T : Any> handleGuest(playerSrc: PlayerSrc, data: T) {
		guestChannels.filter { it.key.isInstance(data) }
			.forEach {
				it.value.send(
					GuestParams(
						playerSrc = playerSrc,
						data = data
					)
				)
			}
	}

	suspend fun handle(player: Player, data: Any) {
		playerChannels.filter { it.key.isInstance(data) }
			.forEach {
				it.value.send(
					PlayerParams(
						player = player,
						data = data
					)
				)
			}
	}

	private fun <T : Any> getPlayerChannel(
		kClass: KClass<T>,
		channelCapacity: Int = CHANNEL_CAPACITY
	): BroadcastChannel<PlayerParams<T>> {

		return playerChannels.getOrPut(kClass) {
			BroadcastChannel(channelCapacity)
		}.unsafeCast<BroadcastChannel<PlayerParams<T>>>()
	}

	private fun <T : Any> getGuestChannel(
		kClass: KClass<T>,
		channelCapacity: Int = CHANNEL_CAPACITY
	): BroadcastChannel<GuestParams<T>> {

		return guestChannels.getOrPut(kClass) {
			BroadcastChannel(channelCapacity)
		}.unsafeCast<BroadcastChannel<GuestParams<T>>>()
	}

	class PlayerParams<T : Any> constructor(
		val data: T,
		val player: Player
	)

	class GuestParams<T : Any> constructor(
		val data: T,
		val playerSrc: PlayerSrc
	)
}
