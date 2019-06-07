package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.stackTrace
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class ModuleLoader(override val coroutineContext: CoroutineContext = createSupervisorJob()) : CoroutineScope {

	private val startQueue = mutableListOf<AbstractModule>()

	private val modules = mutableListOf<AbstractModule>()

	private val loadedModulesRepository = Repository<AbstractModule>()

	private var isStarted = false

	suspend fun add(module: AbstractModule, manualStart: Boolean = false) {
		try {
			if (isStarted) throw IllegalStateException("you have to call ModuleLoader::add() before ModuleLoader::startAll()")

			module.moduleLoader = this
			module.onInit()

			if (!manualStart) {
				startQueue.add(module)
			}
		} catch (exception: Throwable) {
			ExceptionsStorage.add(
				Exception(
					"failed to load module ${module::class.simpleName}: \r\n${exception.message}\r\n ${exception.cause}",
					exception
				)
			)
		}
	}

	suspend fun startAll() {
		isStarted = true

		launch {
			startQueue.forEach { module ->
				launch {
					start(module)
				}
			}
			startQueue.clear()
		}.join()
	}

	suspend fun start(module: AbstractModule) {
		try {
			Console.log("start module ${module::class.simpleName}")
			withTimeout(MODULE_LOADING_TIMEOUT) { module.onStart()?.join() }
			Console.log("loaded module ${module::class.simpleName}")

			modules.add(module)

			loadedModulesRepository
				.getChannel(module::class)
				.send(module)

		} catch (exception: Throwable) {
			ExceptionsStorage.add(
				Exception(
					"failed to start module ${module::class.simpleName}: \n" + exception.stackTrace(), exception
				)
			)
		}
	}

	fun stop() = launch {
		modules.asReversed().forEach {
			try {
				Console.log("stop module ${it::class.simpleName}")
				it.onStop()?.join()
				it.cancel()
			} catch (exception: Throwable) {
				Console.error(
					"failed to stop module ${it::class.simpleName}: \n" +
							"${exception.message}\n" +
							" ${exception.cause}"
				)
			}
		}
		modules.clear()
		isStarted = false
	}

	fun getLoadedModules(): List<AbstractModule> = modules

	suspend fun <T : AbstractModule> getModule(kClass: KClass<T>) =
		loadedModulesRepository.getChannel(kClass).openSubscription().receiveAndCancel()

	private companion object {
		const val MODULE_LOADING_TIMEOUT: Long = 60_000
	}
}