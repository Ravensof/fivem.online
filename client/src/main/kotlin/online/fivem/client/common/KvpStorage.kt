package online.fivem.client.common

import online.fivem.client.gtav.Client
import online.fivem.common.Serializer
import online.fivem.common.other.Serializable

object KvpStorage {

	fun delete(key: String) = Client.deleteResourceKvp(key)

	fun set(key: String, value: String) = Client.setResourceKvp(key, value)

	fun set(key: String, value: Float) = Client.setResourceKvpFloat(key, value)

	fun set(key: String, value: Int) = Client.setResourceKvpInt(key, value)

	fun set(key: String, value: Serializable) = set(key, Serializer.serialize(value))

	fun getFloat(key: String): Float = Client.getResourceKvpFloat(key)

	fun getInt(key: String): Int = Client.getResourceKvpInt(key)

	fun getString(key: String): String? = Client.getResourceKvpString(key)

	inline fun <reified R : Any> getSerializable(key: String): R? {
		return getString(key)?.let {
			Serializer.deserialize(string = it).unsafeCast<R>()
		}
	}

	fun getKeys(prefix: String): List<String> {
		val handle = Client.startFindKvp(prefix)

		val list = mutableListOf<String>()

		while (true) {
			val data = Client.findKvp(handle) ?: break
			list.add(data)
		}

		Client.endFindKvp(handle)

		return list
	}

}