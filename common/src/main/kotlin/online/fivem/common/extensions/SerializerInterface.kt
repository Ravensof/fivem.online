package online.fivem.common.extensions

import online.fivem.common.other.KotlinXSerializationPacket
import online.fivem.common.other.Serializable
import online.fivem.common.other.SerializerInterface

inline fun <reified R> SerializerInterface.deserialize(
	string: String,
	serializerId: Int = getSerializerHash(R::class)
): R {

	val result = deserialize(string = string, serializerId = serializerId)

	if (result !is R) {
		throw SerializerInterface.DeserializationException("can't deserialize object ${result::class.simpleName} as ${R::class.simpleName}")
	}

	return result
}

fun SerializerInterface.deserialize(kotlinXSerializationPacket: KotlinXSerializationPacket) = deserialize(
	serializerId = kotlinXSerializationPacket.hash,
	string = kotlinXSerializationPacket.serialized
)

fun <T : Serializable> SerializerInterface.serializeToPacket(obj: T) = KotlinXSerializationPacket(
	hash = getSerializerHash(obj::class),
	serialized = serialize(obj)
)