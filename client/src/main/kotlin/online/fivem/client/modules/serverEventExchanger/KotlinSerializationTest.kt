package online.fivem.client.modules.serverEventExchanger

import online.fivem.common.common.AbstractModule
import online.fivem.common.common.KSerializer
import online.fivem.common.events.net.KotlinXSerializationPacket

class KotlinSerializationTest : AbstractModule() {
	override fun onInit() {
		ServerEvent.on<KotlinXSerializationPacket> { handle(it.hash, it.serialized) }
	}

	private fun handle(hash: Int, serialized: String) {
		val deserialized = KSerializer.deserialize(hash, serialized) ?: DeSerializationException()

		ServerEvent.handle(deserialized)
	}

	class DeSerializationException : Exception()
}