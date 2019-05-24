package online.fivem.server.modules.client_event_exchanger

import online.fivem.common.entities.PlayerSrc
import online.fivem.common.other.Serializable
import online.fivem.server.entities.Player
import kotlin.reflect.KClass

object ClientEvent {

	val guestHandlers = mutableListOf<Pair<KClass<*>, suspend (PlayerSrc, Any) -> Unit>>()
	val playerHandlers = mutableListOf<Pair<KClass<*>, suspend (Player, Any) -> Unit>>()

	suspend fun emit(
		data: Serializable,
		player: Player
	) = emit(data, player.playerSrc)

	suspend fun emit(
		data: Serializable,
		playerSrc: PlayerSrc? = null
	) {
		ClientEventExchangerModule.channel.send(
			ClientEventExchangerModule.SendingPacket(
				playerSrc = playerSrc,
				data = data
			)
		)
	}

	inline fun <reified T : Any> on(noinline function: suspend (Player, T) -> Unit) {
		playerHandlers.add(T::class to function.unsafeCast<(suspend (Player, Any) -> Unit)>())
	}

	inline fun <reified T : Any> onGuest(noinline function: suspend (PlayerSrc, T) -> Unit) {
		guestHandlers.add(T::class to function.unsafeCast<(suspend (PlayerSrc, Any) -> Unit)>())
	}

	suspend fun handleGuest(playerSrc: PlayerSrc, data: Any) {
		guestHandlers.forEach {
			if (it.first.isInstance(data)) {
				it.second(playerSrc, data)
			}
		}
	}

	suspend fun handle(player: Player, data: Any) {
		playerHandlers.forEach {
			if (it.first.isInstance(data)) {
				it.second(player, data)
			}
		}
	}
}
