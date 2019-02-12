package online.fivem.server.extensions

import external.nodejs.mysql.Connection
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.extensions.onNull

suspend fun <T> Connection.row(query: String, params: Any? = null): T? {

	val pauseChannel = Channel<T?>()

	val callback = { error: String?, results: Connection.Results, fields: Array<Connection.Field> ->

		error?.let {
			pauseChannel.close()
			throw Exception(it)
		}

		GlobalScope.launch {
			if (results.toArray<Any>().isEmpty()) return@launch pauseChannel.send(null)

			pauseChannel.send(results[0])
		}
		Unit
	}

	params?.let {
		this.query("$query LIMIT 1", it, callback)
	}.onNull {
		this.query("$query LIMIT 1", callback)
	}

	return pauseChannel.receive()
}

fun <T> Connection.rowAsync(query: String, params: Any? = null): Deferred<T?> = GlobalScope.async {
	row<T>(query, params)
}

suspend fun Connection.send(query: String, params: Any? = null): Connection.Results {

	val pauseChannel = Channel<Connection.Results>()

	val callback = { error: String?, results: Connection.Results, fields: Array<Connection.Field> ->
		GlobalScope.launch {
			pauseChannel.send(results)
		}
		Unit
	}

	params?.let {
		this.query(query, it, callback)
	}.onNull {
		this.query(query, callback)
	}

	return pauseChannel.receive()
}

fun Connection.sendAsync(query: String, params: Any? = null) = GlobalScope.async {
	send(query, params)
}