package online.fivem.server.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import online.fivem.server.gtav.Natives
import kotlin.coroutines.CoroutineContext

val Dispatchers.Native: CoroutineDispatcher
	get() = NativeDispatcher

private object NativeDispatcher : CoroutineDispatcher() {
	override fun dispatch(context: CoroutineContext, block: Runnable) {
		Natives.mainThread {
			block.run()
		}
	}
}