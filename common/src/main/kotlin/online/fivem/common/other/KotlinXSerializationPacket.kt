package online.fivem.common.other

import kotlinx.serialization.Serializable
import online.fivem.common.common.KSerializer

@Serializable
open class KotlinXSerializationPacket(
	val hash: Int,
	val serialized: String
) {
	fun deserialize(): Any? {
		return KSerializer.deserialize(hash, serialized)
	}
}