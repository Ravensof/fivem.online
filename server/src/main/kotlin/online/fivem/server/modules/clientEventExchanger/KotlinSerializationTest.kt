package online.fivem.server.modules.clientEventExchanger

import online.fivem.common.common.AbstractModule
import online.fivem.common.common.KSerializer
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.net.KotlinXSerializationPacket

class KotlinSerializationTest : AbstractModule() {
	override fun onInit() {
		ClientEvent.on<KotlinXSerializationPacket> { playerSrc, packet ->
			handle(playerSrc, packet.hash, packet.serialized)
		}
	}

	private fun handle(playerSrc: PlayerSrc, hash: Int, serialized: String) {
		val deserialized = KSerializer.deserialize(hash, serialized) ?: DeSerializationException()

		ClientEvent.handle(playerSrc, deserialized)
	}

	class DeSerializationException : Exception()
}