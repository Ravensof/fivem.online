package online.fivem.common.other

import kotlin.reflect.KClass

interface SerializerInterface {

	fun <T : Any> serialize(obj: T): String

	fun deserialize(serializerId: Int, string: String): Any?

	fun getSerializerHash(kClass: KClass<*>): Int

	fun deserialize(kotlinXSerializationPacket: KotlinXSerializationPacket): Any? {
		return deserialize(kotlinXSerializationPacket.hash, kotlinXSerializationPacket.serialized)
	}

	fun <T : Any> serializeToPacket(obj: T): KotlinXSerializationPacket {
		return KotlinXSerializationPacket(
			hash = getSerializerHash(obj::class),
			serialized = serialize(obj)
		)
	}

}