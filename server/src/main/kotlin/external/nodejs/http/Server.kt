package external.nodejs.http

external interface Server {
	fun listen(port: Int)
	fun listen(port: Int, host: String)
	fun listen(port: Int, host: String, backlog: Number)

	fun listen(port: Int, callback: (String?) -> Unit)
	fun listen(callback: () -> Unit)
}