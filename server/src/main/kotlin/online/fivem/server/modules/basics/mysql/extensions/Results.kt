package online.fivem.server.modules.basics.mysql.extensions

import external.nodejs.mysql.Connection

operator fun <T> Connection.Results.get(index: Int): T {
	return this.asDynamic()[index].unsafeCast<T>()
}

fun <T> Connection.Results.toArray(): Array<T> {
	return this.unsafeCast<Array<T>>()
}

fun <T> Connection.Results.forEach(function: (T) -> Unit) {
	toArray<T>().forEach { function(it) }
}