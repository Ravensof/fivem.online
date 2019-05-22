package online.fivem.common.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import online.fivem.common.other.SerializerInterface
import kotlin.reflect.KClass

internal class KSerializer : SerializerInterface {
	private val serializers: MutableMap<KClass<*>, KSerializer<*>> = mutableMapOf()
	private val classHashes: MutableMap<Int, KClass<*>> = mutableMapOf()
	private val reversedClassHashes: MutableMap<KClass<*>, Int> = mutableMapOf()

	private var key = 0

	override fun getSerializerHash(kClass: KClass<*>): Int {
		return reversedClassHashes[kClass] ?: throw UnregisteredClassException(kClass)
	}

	override fun <T : Any> serialize(obj: T): String {
		val serializer = getSerializer(obj::class) ?: throw UnregisteredClassException(obj::class)

		return Json.indented.stringify(serializer, obj)
	}

	override fun deserialize(string: String, serializerId: Int): Any {
		val serializer =
			getSerializer(serializerId)
				?: throw UnregisteredClassException("cannot find deserializer for hash = $serializerId")

		return Json.parse(serializer, string) ?: throw IllegalStateException("cannot deserialize $string")
	}

	internal fun add(kClass: KClass<*>, serializer: KSerializer<*>) {
		serializers[kClass] = serializer
		(++key).let {
			classHashes[it] = kClass
			reversedClassHashes[kClass] = it
		}
	}

	internal fun add(map: Map<KClass<*>, KSerializer<*>>) = map.forEach {
		add(it.key, it.value)
	}

	internal fun add(vararg pairs: Pair<KClass<*>, KSerializer<*>>) = pairs.forEach {
		add(it.first, it.second)
	}

	private fun getSerializer(hash: Int): KSerializer<*>? {
		classHashes[hash]?.let {
			return serializers[it]
		}
		return null
	}

	private fun <T : Any> getSerializer(kClass: KClass<out T>): KSerializer<T>? {
		return serializers[kClass]?.unsafeCast<KSerializer<T>>()
	}

	class UnregisteredClassException : Exception {

		constructor(
			kClass: KClass<out Any>? = null,
			message: String = "class ${kClass?.simpleName} is not registered as serializable"
		) : super(message)

		constructor(string: String) : this(message = string)
	}
}