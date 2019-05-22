package online.fivem.common.other

class ClientsNetPacket(
	hash: Int,
	serialized: String,

	val playersCount: Int,
	val key: Double?
) : KotlinXSerializationPacket(hash, serialized) {

	constructor(
		kotlinXSerializationPacket: KotlinXSerializationPacket,
		playersCount: Int,
		key: Double?
	) : this(
		hash = kotlinXSerializationPacket.hash,
		serialized = kotlinXSerializationPacket.serialized,
		playersCount = playersCount,
		key = key
	)
}