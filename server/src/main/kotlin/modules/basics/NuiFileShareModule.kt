package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.server.external.Request
import online.fivem.server.external.Response
import online.fivem.server.gtav.Natives
import kotlin.coroutines.CoroutineContext

class NuiFileShareModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {
	private val httpServerModule by moduleLoader.onReady<HttpServerModule>()
	private val disposable = mutableListOf<Int>()

	override fun start(): Job? {
		shareList.forEach { disposable += httpServerModule.handlers.add(it to ::handler) }

		return super.start()
	}

	override fun stop(): Job? {
		disposable.forEach { httpServerModule.handlers.remove(it) }

		return super.stop()
	}

	private fun handler(request: Request, response: Response) {
//		Console.debug("nuiFileShare: ${request.address} to ${request.path} ")

		val file = Natives.loadResourceFile(GlobalConfig.MODULE_NAME, request.path)

		if (file != null) {
			Console.debug("nuiFileShare: ${request.address} sending ${request.path} ")
			return response.send(file)
		}
		Console.debug("nuiFileShare: ${request.address} no file ${request.path} ")

		response.writeHead("404")
		response.send("404")
	}

	companion object {
		private val shareList = listOf(
			Regex("^/client/.*"),
			Regex("^/common/.*"),
			Regex("^/nui/.*")
		)
	}
}