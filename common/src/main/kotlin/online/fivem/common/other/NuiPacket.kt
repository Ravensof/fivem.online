package online.fivem.common.other

class NuiPacket(
	hash: Int,
	serialized: String
) : KotlinXSerializationPacket(hash, serialized) {

	constructor(kotlinXSerializationPacket: KotlinXSerializationPacket) : this(
		kotlinXSerializationPacket.hash,
		kotlinXSerializationPacket.serialized
	)
}