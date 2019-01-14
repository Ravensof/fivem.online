package online.fivem.server.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import online.fivem.common.external.Base64
import online.fivem.server.ServerConfig
import online.fivem.server.gtav.Exports
import kotlin.coroutines.CoroutineContext

open class MySQL(override val coroutineContext: CoroutineContext) : CoroutineScope {

	fun connect() {

	}

	fun close() {

	}

	fun <Type> query(query: String, vararg args: Any?): Deferred<Array<Type>> {
		return async {
			val response = sQuery(query, *args).await()

			val result = JSON.parse<MySQLResponse<Type>>(response)

			if (result.code != 0) throw MySQLException(result.response.joinToString("\r\n"))

			return@async result.response
		}
	}

	fun query(query: String, vararg args: Any?): Deferred<Int?> {
		return async {
			val response = sQuery(query, *args).await()
			val result = JSON.parse<MySQLResponse<Any>>(response)

			if (result.code != 0) throw MySQLException(result.response.joinToString("\r\n"))

			return@async result.insert_id
		}
	}

	fun send(query: String, vararg args: Any?) {
		@Suppress("DeferredResultUnused")
		query(query, *args)
	}

	private fun sQuery(query: String, vararg args: Any?): Deferred<String> {
		@Suppress("NAME_SHADOWING")
		val query = insertArgs(query, args)
//		Console.debug(query)

		return Exports.performHttpRequest(
			coroutineScope = this,
			url = ServerConfig.MYSQL_HTTP_API,
			data = mapOf("request" to query),
			httpRequestType = "POST"
		)
	}

	private fun insertArgs(query: String, args: Array<out Any?>): String {
		var string = query

		args.forEach {

			string = string.replaceFirst(
				"?", when (it) {
					null -> "NULL"

					is Int -> it.toString()

					else -> filter(it.toString())
				}
			)
		}

		return string
	}

	private class MySQLResponse<T>(
		val code: Int,
		val response: Array<T>,
		val insert_id: Int?
	)

	class MySQLException(message: String) : Throwable(message)

	companion object {
		fun filter(value: String): String {
			return "FROM_BASE64('${Base64.encode(value)}')"
		}
	}
}