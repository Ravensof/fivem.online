package online.fivem.common.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import online.fivem.common.initSerializableClasses
import kotlin.reflect.KClass

object KSerializer {
	private val serializers: MutableMap<KClass<*>, KSerializer<*>> = mutableMapOf()
	private val classHashes: MutableMap<Int, KClass<*>> = mutableMapOf()
	private val reversedClassHashes: MutableMap<KClass<*>, Int> = mutableMapOf()

	private var key = 0

	init {
		initSerializableClasses()
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

	fun getSerializer(hash: Int): KSerializer<*>? {
		classHashes[hash]?.let {
			return serializers[it]
		}
		return null
	}

	fun getSerializerHash(kClass: KClass<*>): Int? {
		return reversedClassHashes[kClass]
	}

	fun <T : Any> getSerializer(kClass: KClass<out T>): KSerializer<T>? {
		return serializers[kClass]?.unsafeCast<KSerializer<T>>()
	}

	fun <T : Any> serialize(obj: T): String {
		val serializer = getSerializer(obj::class) ?: throw UnregisteredClassException()

		return Json.indented.stringify(serializer, obj)
	}

	inline fun <reified T : Any> deserialize(string: String): T {
		val serializer = getSerializer(T::class) ?: throw UnregisteredClassException()

		return Json.parse(serializer, string)
	}

	fun deserialize(hash: Int, string: String): Any? {
		val serializer = getSerializer(hash) ?: throw UnregisteredClassException()

		return Json.parse(serializer, string)
	}

	class UnregisteredClassException : Exception()
}