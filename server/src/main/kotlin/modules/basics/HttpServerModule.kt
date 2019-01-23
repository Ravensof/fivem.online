package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Stack
import online.fivem.server.external.Request
import online.fivem.server.external.Response
import online.fivem.server.gtav.Exports
import kotlin.coroutines.CoroutineContext

class HttpServerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {
	val handlers = Stack<Pair<Regex, (Request, Response) -> Unit>>()

	init {
		handlers.add(Regex(".*") to ::handler404)
	}

	override fun start(): Job? {
		Exports.setHttpHandler(::handler)

		return super.start()
	}

	override fun stop(): Job? {
		handlers.clear()

		return super.stop()
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