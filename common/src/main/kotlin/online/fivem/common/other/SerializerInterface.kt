package online.fivem.common.other

import kotlin.reflect.KClass

interface SerializerInterface {

	fun <T : Serializable> serialize(obj: T): String

	fun deserialize(string: String, serializerId: Int = -1): Any

	fun getSerializerHash(kClass: KClass<*>): Int

	class DeserializationException(message: String) : Throwable(message)
}