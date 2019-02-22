package external.nodejs.express

import external.nodejs.http.ConnectionListener
import kotlin.js.RegExp

external interface App : ConnectionListener {

	fun use(path: String, callback: (Request, Response, () -> Unit) -> Unit)

	fun use(callback: (Request, Response, () -> Unit) -> Unit)

	fun get(path: String, function: (Request, Response) -> Unit)

	fun post(path: String, function: (Request, Response) -> Unit)

	fun all(path: String, function: (Request, Response) -> Unit)

	fun get(expression: RegExp, function: (Request, Response) -> Unit)

	fun post(expression: RegExp, function: (Request, Response) -> Unit)

	fun all(expression: RegExp, function: (Request, Response) -> Unit)

	fun listen(port: Int, function: (String?) -> Unit): Server
}