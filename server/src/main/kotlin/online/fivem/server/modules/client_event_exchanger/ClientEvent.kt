package online.fivem.server.modules.client_event_exchanger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.Console
import online.fivem.common.entities.PlayerSrc
import online.fivem.server.entities.Player
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

open class ClientEvent : CoroutineScope {

	override val coroutineContext: CoroutineContext = Job()

	open val printType = "net"

	val handlers = mutableListOf<Pair<KClass<*>, (PlayerSrc, Any) -> Unit>>()
	val playerHandlers = mutableListOf<Pair<KClass<*>, (Player, Any) -> Unit>>()

	fun emit(
		data: Any,
		player: Player
	): Job = emit(data, player.playerSrc)

	fun emit(
		data: Any,
		playerSrc: PlayerSrc? = null
	): Job {
		return launch {
			ClientEventExchangerModule.channel.send(
				ClientEventExchangerModule.Packet(
					playerSrc = playerSrc,
					data = data
				)
			)
		}
	}

	inline fun <reified T : Any> on(noinline function: (Player, T) -> Unit) {
		playerHandlers.add(T::class to function.unsafeCast<((Player, Any) -> Unit)>())

		Console.info("$printType event ${T::class} registered")
	}

	inline fun <reified T : Any> onGuest(noinline function: (PlayerSrc, T) -> Unit) {
		handlers.add(T::class to function.unsafeCast<((PlayerSrc, Any) -> Unit)>())

		Console.info("$printType event ${T::class} registered")
	}

	inline fun <reified T : Any> once(noinline function: (PlayerSrc, T) -> Unit) {
		var handler: (PlayerSrc, T) -> Unit = { _, _ -> }

		handler = { playerSrc, data ->
			function(playerSrc, data)
			unSubscribe(handler)
		}

		@Suppress("UNCHECKED_CAST")
		handlers.add(T::class to handler.unsafeCast<((PlayerSrc, Any) -> Unit)>())

		Console.info("$printType event ${T::class} registered")
	}

	inline fun <reified T : Any> unSubscribe(noinline function: (PlayerSrc, T) -> Unit) {
		handlers.forEach {
			if (it.second == function) {
				handlers.remove(it)
				Console.info("$printType event ${T::class} unsubscribed")
			}
		}
	}

	fun handle(playerSrc: PlayerSrc, data: Any) {
		handlers.forEach {
			if (it.first.isInstance(data)) {
				it.second(playerSrc, data)
			}
		}
	}

	fun handle(player: Player, data: Any) {
		playerHandlers.forEach {
			if (it.first.isInstance(data)) {
				it.second(player, data)
			}
		}
	}

	companion object : ClientEvent()
}
