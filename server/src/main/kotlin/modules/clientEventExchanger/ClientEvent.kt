package online.fivem.server.modules.clientEventExchanger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.common.common.UEvent
import online.fivem.common.entities.PlayerSrc
import kotlin.reflect.KClass

object ClientEvent : UEvent() {

	override val printType = "net"

	fun emit(playerSrc: PlayerSrc, data: Any): Job {
		return GlobalScope.launch {
			ClientEventExchanger.channel.send(
				ClientEventExchanger.Packet(
					playerSrc = playerSrc,
					data = data
				)
			)
		}
	}

	fun emit(data: Any) {
		emit(data::class, data)
	}

	override fun emit(kClass: KClass<out Any>, data: Any): Job {
		return GlobalScope.launch {
			ClientEventExchanger.channel.send(ClientEventExchanger.Packet(data = data))
		}
	}

	//todo убрать родительскую on, т.к. она не будет работать
	inline fun <reified T> on(noinline callback: (PlayerSrc, T) -> Unit) {
		handlers.add {
			val packet = it as ClientEventExchanger.Packet

			if (packet.data is T) {
				callback(packet.playerSrc!!, packet.data)
			}
		}
	}

	fun handle(data: ClientEventExchanger.Packet) {
		super.emit(data::class, data)
	}
}