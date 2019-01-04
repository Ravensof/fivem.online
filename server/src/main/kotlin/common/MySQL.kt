package online.fivem.server.common

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import online.fivem.common.external.Base64
import online.fivem.server.ServerConfig
import online.fivem.server.gtav.Exports

open class MySQL {

	fun connect() {

	}

	fun close() {

	}

	fun <Type> query(query: String): Deferred<Array<Type>> {
		return GlobalScope.async {
			val response = squery(query).await()

			val result = JSON.parse<MySQLResponse<Type>>(response)

			@Suppress("CAST_NEVER_SUCCEEDS")
			if (result.code != 0) throw MySQLException(result.response.joinToString("\r\n"))

			return@async result.response
		}
	}

	fun query(query: String) {
		@Suppress("DeferredResultUnused")
		squery(query)
	}

	private fun squery(query: String): Deferred<String> {
		return Exports.performHttpRequest(
			url = ServerConfig.MYSQL_HTTP_API,
			data = mapOf("request" to query),
			httpRequestType = "POST"
		)
	}

	private class MySQLResponse<T>(
		val code: Int,
		val response: Array<T>
	)

	class MySQLException(message: String) : Throwable(message)

	companion object {

		val instance by lazy {
			MySQL().apply {
				connect()
			}
		}

		fun filter(value: String): String {
			return "FROM_BASE64('${Base64.encode(value)}')"
		}
	}
}