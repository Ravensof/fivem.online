package online.fivem.common.other

import kotlinx.serialization.Serializable

@Serializable
open class KotlinXSerializationPacket(
	val hash: Int,
	val serialized: String
)