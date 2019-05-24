package online.fivem.server.extensions

import external.nodejs.mysql.Connection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.extensions.onNull
import online.fivem.common.extensions.receiveAndCancel

private class Result(
	val results: Connection.Results?,
	val error: Connection.Error?
)

private suspend fun Connection.q(query: String, params: Any? = null): Connection.Results? {
	val pauseChannel = Channel<Result>()

	val callback = { error: Connection.Error?, results: Connection.Results?, fields: Array<Connection.Field> ->
		GlobalScope.launch {
			pauseChannel.send(Result(results, error))
		}
		Unit
	}

	params?.let {
		query(query, it, callback)
	}.onNull {
		query(query, callback)
	}

	val result = pauseChannel.receiveAndCancel()

	result.error?.let {
		throw Exception(
			"${it.code}: " + if (it.sqlMessage != null && it.sql != null) {
				it.sqlMessage + "\r\n" + it.sql
			} else {
				"error :" + JSON.stringify(it) + "\r\n" + JSON.stringify(result.results)
			}
		)
	}

	return result.results
}

suspend fun <T> Connection.row(query: String, params: Any? = null): T? {
	return q("$query LIMIT 1", params)?.toArray<Any>()?.firstOrNull().unsafeCast<T?>()
}

suspend fun Connection.send(query: String, params: Any? = null): Connection.Results {
	return q(query, params)!!
}