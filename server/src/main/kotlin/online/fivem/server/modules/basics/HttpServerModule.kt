package online.fivem.server.modules.basics

import external.exports.http_handler.Request
import external.exports.http_handler.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Stack
import online.fivem.server.gtav.Exports
import kotlin.coroutines.CoroutineContext

class HttpServerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {
	val handlers = Stack<Pair<Regex, (Request, Response) -> Unit>>()

	init {
		handlers.add(Regex(".*") to ::handler404)
	}

	override fun onStart(): Job? {
		Exports.setHttpHandler(::handler)

		return super.onStart()
	}

	override fun onStop(): Job? {
		handlers.clear()

		return super.onStop()
	}

	private fun handler(request: Request, response: Response) {
		handlers.reversed().forEach {
			if (request.path.matches(it.first)) {
				return it.second(request, response)
			}
		}

		response.writeHead("404")
	}

	private fun handler404(request: Request, response: Response) {
		Console.debug("HttpServerModule: ${request.address} requests ${request.path} ")

		response.writeHead("404")
		response.send("404")
	}
}