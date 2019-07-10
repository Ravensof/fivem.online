package online.fivem.client.common

import online.fivem.Natives
import online.fivem.common.Serializer
import online.fivem.common.other.Serializable

object KvpStorage {

	fun delete(key: String) = Natives.deleteResourceKvp(key)

	fun set(key: String, value: String) = Natives.setResourceKvp(key, value)

	fun set(key: String, value: Float) = Natives.setResourceKvpFloat(key, value)

	fun set(key: String, value: Int) = Natives.setResourceKvpInt(key, value)

	fun set(key: String, value: Serializable) = set(key, Serializer.serialize(value))

	fun getFloat(key: String): Float = Natives.getResourceKvpFloat(key)

	fun getInt(key: String): Int = Natives.getResourceKvpInt(key)

	fun getString(key: String): String? = Natives.getResourceKvpString(key)

	inline fun <reified R : Any> getSerializable(key: String): R? {
		return getString(key)?.let {
			Serializer.deserialize(string = it).unsafeCast<R>()
		}
	}

	fun getKeys(prefix: String): List<String> {
		val handle = Natives.startFindKvp(prefix)

		val list = mutableListOf<String>()

		while (true) {
			val data = Natives.findKvp(handle) ?: break
			list.add(data)
		}

		Natives.endFindKvp(handle)

		return list
	}

}