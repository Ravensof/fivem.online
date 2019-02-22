package external.nodejs.http

import external.nodejs.express.Response

external interface HTTP {

	fun createServer(): Server
	fun createServer(callback: (Request, Response) -> Unit): Server
	fun createServer(connectionListener: ConnectionListener): Server
}
