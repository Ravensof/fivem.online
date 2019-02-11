package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.events.local.ModuleLoadedEvent
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ModuleLoader : CoroutineScope {

	override val coroutineContext: CoroutineContext = Job()

	private val queue = Channel<AbstractModule>(128)
	private var finally: (() -> Unit)? = null

	private val modules = mutableListOf<AbstractModule>()

	private val events = Event(coroutineContext)

	fun add(module: AbstractModule) {
		try {
			module.moduleLoader = this@ModuleLoader
			module.onInit()

			launch {
				if (queue.isFull) {
					Console.warn("ModuleLoader`s queue is full")
				}
				queue.send(module)
			}
		} catch (exception: Throwable) {
			Console.error("failed to load module ${module::class.simpleName}: \r\n${exception.message}\r\n ${exception.cause}")
		}
	}

	fun finally(function: () -> Unit) {
		finally = function
	}

	fun start() = launch {

		var module: AbstractModule

		while (!queue.isEmpty) {
			module = queue.receive()

			try {
				Console.log("start module ${module::class.simpleName}")
				module.onStart()?.join()
				modules.add(module)

				val event = ModuleLoadedEvent(module)

				UEvent.emit(event)
				events.emit(module)
			} catch (exception: Throwable) {
				Console.error(
					"failed to start module ${module::class.simpleName}: \n" +
							"${exception.message}\n" +
							" ${exception.cause}"
				)
			}
		}
		finally?.invoke()
	}

	fun stop() = launch {
		modules.asReversed().forEach {
			try {
				Console.log("stop module ${it::class.simpleName}")
				modules.remove(it)
				it.onStop()?.join()
			} catch (exception: Throwable) {
				Console.error(
					"failed to stop module ${it::class.simpleName}: \n" +
							"${exception.message}\n" +
							" ${exception.cause}"
				)
			}
		}
	}

	inline fun <reified T : AbstractModule> on(noinline function: (T) -> Unit) {
		on(T::class, function)
	}

	fun <T : AbstractModule> on(kClass: KClass<T>, function: (T) -> Unit) {
		events.on(kClass, function)
	}

	inline fun <reified ModuleType : AbstractModule> onReady(): OnLocalModuleLoaded<ModuleType> {
		return OnLocalModuleLoaded(ModuleType::class)
	}

//	companion object {
//		inline fun <reified ModuleType : AbstractModule> onReady(): OnGlobalModuleLoaded<ModuleType> {
//			return OnGlobalModuleLoaded(ModuleType::class)
//		}
//	}

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

//	class OnGlobalModuleLoaded<T : AbstractModule>(private val kClass: KClass<T>) {
//		private var value: T? = null
//
//		init {
//			UEvent.on<ModuleLoadedEvent> {
//				if (kClass.isInstance(it)) {
//					value = it.module.unsafeCast<T>()
//				}
//			}
//		}
//
//		operator fun getValue(thisRef: AbstractModule, property: KProperty<*>): T {
//			value?.let {
//				return it
//			}
//
//			throw Exception("module ${kClass.simpleName} used in ${property.name} have not been loaded")
//		}
//	}

	private class Event(coroutineContext: CoroutineContext) : UEvent(coroutineContext) {
		fun <T : AbstractModule> on(kClass: KClass<T>, function: (T) -> Unit) {
			handlers.add(kClass to function.unsafeCast<(Any) -> Unit>())
		}
	}
}