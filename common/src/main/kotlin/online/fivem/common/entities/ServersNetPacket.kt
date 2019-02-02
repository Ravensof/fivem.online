package online.fivem.common.entities

class ServersNetPacket(
	hash: Int,
	serialized: String
) : KotlinXSerializationPacket(hash, serialized)