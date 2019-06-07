package online.fivem.client.common

interface IObjectIterator<T> : Iterator<T> {

	fun close()

	fun filter(predicate: (T) -> Boolean): List<T> {
		val list = mutableListOf<T>()

		try {
			forEach {
				if (!predicate(it)) return@forEach

				list.add(it)
			}
		} finally {
			close()
		}

		return list
	}

}