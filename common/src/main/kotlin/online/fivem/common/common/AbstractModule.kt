package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import online.fivem.common.extensions.forEach
import kotlin.coroutines.CoroutineContext

abstract class AbstractModule : CoroutineScope {

	override val coroutineContext: CoroutineContext = createSupervisorJob()

	lateinit var moduleLoader: ModuleLoader

	open fun onInit() {}

	open fun onStart(): Job? = null

	open fun onStop(): Job? {

		coroutineContext.cancel()

		return null
	}

	protected inline fun <reified T : Any> Event.on(noinline action: suspend (T) -> Unit): ReceiveChannel<T> {
		val channel = openSubscription(T::class)

		this@AbstractModule.launch {
			channel.forEach(action)
		}

		return channel
	}

	protected fun Event.emitAsync(data: Any) = launch {
		emit(data)
	}
}