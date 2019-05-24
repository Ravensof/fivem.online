package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import online.fivem.common.events.local.ModuleLoadedEvent
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.stackTrace
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ModuleLoader(override val coroutineContext: CoroutineContext = createJob()) : CoroutineScope {

	private val queue = mutableListOf<AbstractModule>()
	private var finally = {}

	private val modules = mutableListOf<AbstractModule>()

	private val loadedModulesRepository = Repository<AbstractModule>()

	private var isStarted = false

	fun add(module: AbstractModule) {
		try {
			if (isStarted) throw IllegalStateException("you have to use ModuleLoader::add() only in AbstractModule::onInit()")

			module.moduleLoader = this@ModuleLoader
			module.onInit()

			queue.add(module)

		} catch (exception: Throwable) {
			Console.error("failed to load module ${module::class.simpleName}: \r\n${exception.message}\r\n ${exception.cause}")
		}
	}

	fun finally(function: () -> Unit) {
		finally = function
	}

	fun start() = launch {
		isStarted = true

		val startJob = launch {
			queue.forEach { module ->
				launch {
					try {
						Console.log("start module ${module::class.simpleName}")
						withTimeout(MODULE_LOADING_TIMEOUT) { module.onStart()?.join() }
						Console.log("loaded module ${module::class.simpleName}")

						modules.add(module)

						val event = ModuleLoadedEvent(module)

						Event.emit(event)

						loadedModulesRepository
							.getChannel(module::class)
							.send(module)

					} catch (exception: Throwable) {
						Console.error(
							"failed to start module ${module::class.simpleName}: \n" + exception.stackTrace()
						)
					}
				}
			}
		}

		startJob.join()

		finally.invoke()
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

	suspend fun getModules() =
		loadedModulesRepository.getChannels().map { it.value.openSubscription().receiveAndCancel() }

	suspend fun <T : AbstractModule> getModule(kClass: KClass<T>) =
		loadedModulesRepository.getChannel(kClass).openSubscription().receiveAndCancel()

	inline fun <reified ModuleType : AbstractModule> delegate(): OnLocalModuleLoaded<ModuleType> {
		return OnLocalModuleLoaded(ModuleType::class)
	}

	class OnLocalModuleLoaded<T : AbstractModule>(private val kClass: KClass<T>) {
		private var value: T? = null

		operator fun getValue(thisRef: AbstractModule, property: KProperty<*>): T {
			value?.let {
				return it
			}

			val module = thisRef.moduleLoader.modules.find { it::class == kClass }?.unsafeCast<T>()
				?: throw Exception("module ${kClass.simpleName} used in ${thisRef::class.simpleName}/${property.name} have not been loaded")

			value = module

			return module
		}
	}

	private companion object {
		const val MODULE_LOADING_TIMEOUT: Long = 60_000
	}
}