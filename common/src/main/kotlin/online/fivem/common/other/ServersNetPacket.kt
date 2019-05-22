package online.fivem.common.other

class ServersNetPacket(
	hash: Int,
	serialized: String
) : KotlinXSerializationPacket(hash, serialized) {

	constructor(kotlinXSerializationPacket: KotlinXSerializationPacket) : this(
		kotlinXSerializationPacket.hash,
		kotlinXSerializationPacket.serialized
	)
}