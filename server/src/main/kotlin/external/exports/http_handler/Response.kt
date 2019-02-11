package external.exports.http_handler

external interface Response {
	fun send(data: String)
	fun writeHead(head: String)
}