package external.exports.http_handler

external interface Request {
	val path: String
	val method: String
	val address: String

	fun setDataHandler(handler: (String) -> Unit)
}

private fun Request.getHeader(name: String): String? {
	return this.asDynamic().headers[name] as? String
}

val Request.headers: Map<String, String?>
	get() {
		return mapOf(
			"Accept-Encoding" to getHeader("Accept-Encoding"),
			"User-Agent" to getHeader("User-Agent"),
			"Host" to getHeader("Host"),
			"Connection" to getHeader("Connection"),
			"Accept-Language" to getHeader("Accept-Language"),
			"Accept" to getHeader("Accept"),
			"Upgrade-Insecure-Requests" to getHeader("Upgrade-Insecure-Requests")
		)
	}