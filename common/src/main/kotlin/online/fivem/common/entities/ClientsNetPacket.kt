package online.fivem.common.entities

class ClientsNetPacket(
	hash: Int,
	serialized: String,

	val playersCount: Int,
	val key: Double?
) : KotlinXSerializationPacket(hash, serialized)