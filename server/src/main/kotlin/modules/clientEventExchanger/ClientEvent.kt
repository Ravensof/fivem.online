package online.fivem.server.modules.clientEventExchanger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.Console
import online.fivem.common.entities.PlayerSrc
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

open class ClientEvent : CoroutineScope {
	/**
	 * Context of this scope.
	 */
	override val coroutineContext: CoroutineContext = Job()

	open val printType = "net"

	val handlers = mutableListOf<Pair<KClass<*>, (PlayerSrc, Any) -> Unit>>()

	fun emit(data: Any, playerSrc: PlayerSrc? = null): Job {
		return launch {
			ClientEventExchangerModule.channel.send(
				ClientEventExchangerModule.Packet(
					playerSrc = playerSrc,
					data = data
				)
			)
		}
	}

	inline fun <reified T : Any> on(noinline function: (PlayerSrc, T) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		handlers.add(T::class to function as (PlayerSrc, Any) -> Unit)

		Console.info("$printType event ${T::class} registered")
	}

	inline fun <reified T : Any> once(noinline function: (PlayerSrc, T) -> Unit) {
		var handler: (PlayerSrc, T) -> Unit = { _, _ -> }

		handler = { playerSrc, data ->
			function(playerSrc, data)
			unSubscribe(handler)
		}

		@Suppress("UNCHECKED_CAST")
		handlers.add(T::class to handler as (PlayerSrc, Any) -> Unit)

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

	fun handle(playerSrc: PlayerSrc, data: Any): Job {
		return launch {
			handlers.forEach {
				if (it.first.isInstance(data)) {
					it.second(playerSrc, data)
				}
			}
		}
	}

	companion object : ClientEvent()
}
