package online.fivem.server.modules.basics

import external.exports.http_handler.Request
import external.exports.http_handler.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.server.gtav.Natives
import kotlin.coroutines.CoroutineContext

class NuiFileShareModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {
	private val httpServerModule by moduleLoader.onReady<HttpServerModule>()
	private val disposable = mutableListOf<Int>()

	override fun onStart(): Job? {
		shareList.forEach { disposable += httpServerModule.handlers.add(it to ::handler) }

		return super.onStart()
	}

	override fun onStop(): Job? {
		disposable.forEach { httpServerModule.handlers.remove(it) }

		return super.onStop()
	}

	//не работает для бинарных файлов
	private fun handler(request: Request, response: Response) {
		val file = Natives.loadResourceFile(GlobalConfig.MODULE_NAME, request.path)

		if (file != null) {
			Console.debug("nuiFileShare: ${request.address} sending ${file.length} ${request.path} ")
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