package online.fivem.server.modules.basics


import external.nodejs.express.Express
import external.nodejs.express.Request
import external.nodejs.express.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Stack
import online.fivem.common.extensions.forEach
import online.fivem.server.gtav.Natives
import require
import kotlin.coroutines.CoroutineContext

class HttpServerModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {
	val handlers = Stack<Pair<Regex, (Request, Response) -> Unit>>()

	private val express = require("express")
	private val app = express().unsafeCast<Express>()

	init {
		handlers.add(Regex(".*") to ::handler404)
	}

	override fun onInit() {
		app.use("/common", express.static(ROOT_DIR + "common"))
		app.use("/nui", express.static(ROOT_DIR + "nui"))

//		app.get(RegExp("/.*"), ::handler)
	}

	override fun onStart(): Job? {

		return launch {
			val pauseChannel = Channel<Unit>()

			app.listen(HTTP_PORT) { error ->
				pauseChannel.close()

				error?.let {
					throw Exception(it)
				}

				Console.info("http server is listening on $HTTP_PORT")
			}

			pauseChannel.forEach { }
		}
	}

	override fun onStop(): Job? {

		handlers.clear()

		return super.onStop()
	}

	private fun handler(request: Request, response: Response) {
		Console.debug(request)
		Console.debug(response)

//		handlers.reversed().forEach {
//			if (request.url.matches(it.first)) {
//				return it.second(request, response)
//			}
//		}

		handler404(request, response)
	}

	private fun handler404(request: Request, response: Response) {
		response.sendStatus(404)
		response.end()
	}

	companion object {
		private const val HTTP_PORT = GlobalConfig.HTTP_PORT
		private val ROOT_DIR = Natives.getResourcePath(Natives.getCurrentResourceName()) + "/"
	}
}