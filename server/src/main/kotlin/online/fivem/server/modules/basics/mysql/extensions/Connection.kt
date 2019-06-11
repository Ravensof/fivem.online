package online.fivem.server.modules.basics.mysql.extensions

import external.danko.Base64
import external.nodejs.mysql.Connection
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.common.CustomScope
import online.fivem.common.extensions.receiveAndCancel


suspend fun <T> Connection.row(query: String, params: Any? = arrayOf<Any>()): T? {
	return q("$query LIMIT 1", params)?.toArray<Any>()?.firstOrNull().unsafeCast<T?>()
}

suspend fun Connection.send(query: String, params: Any? = arrayOf<Any>()): Connection.Results {
	return q(query, params)!!
}

suspend fun <T> Connection.fetch(query: String, params: Any? = arrayOf<Any>()) = q(query, params)!!.toArray<T>()

private suspend fun Connection.q(query: String, params: Any? = arrayOf<Any>()): Connection.Results? {
	val pauseChannel = Channel<Result>()

	val callback = { error: Connection.Error?, results: Connection.Results?, fields: Array<Connection.Field> ->
		CustomScope.launch {
			pauseChannel.send(Result(results, error))
		}
		Unit
	}

	when (params) {
		is Array<*> -> {
			val (query, params) = replaceNulls(query, params)

			query(query, params, callback)
		}

		is Any -> {
			query(query, params, callback)
		}

		else -> {
			query(replaceFirstNull(query), callback)
		}
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


private fun <T> replaceNulls(query: String, args: Array<T>): Pair<String, Array<T>> {

	if (args.all { it != null }) return query to args

	var string = query

	val list = args.toMutableList()

	args.forEach {

		string = if (it == null) {
			replaceFirstNull(query)

		} else {
			string.replaceFirst(Regex("=\\s*\\?"), "=FROM_BASE64('${Base64.encode(it.toString())}')")
		}

		list.remove(it)
	}

	return string to list.toTypedArray()
}

private fun replaceFirstNull(query: String): String {

	val replace = query.replaceFirst(Regex(".*?(!?\\s*=\\s*\\?).*"), "$1")

	return query.replaceFirst(replace, if (replace[0] == '!') " is NOT NULL" else " is NULL")
}

private class Result(
	val results: Connection.Results?,
	val error: Connection.Error?
)