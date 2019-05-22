package online.fivem.common.other

import kotlinx.serialization.Serializable
import online.fivem.common.Serializer
import online.fivem.common.extensions.deserialize

@Serializable
open class KotlinXSerializationPacket(
	val hash: Int,
	val serialized: String
) {
	fun deserialize(): Any? {
		return Serializer.deserialize(this)
	}
}