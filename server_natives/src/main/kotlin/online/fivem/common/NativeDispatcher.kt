package online.fivem.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import online.fivem.common.common.ExceptionsStorage
import kotlin.coroutines.CoroutineContext

class NativeDispatcher : CoroutineDispatcher() {
	override fun dispatch(context: CoroutineContext, block: Runnable) {
		setImmediate {
			try {
				block.run()
			} catch (throwable: Throwable) {
				ExceptionsStorage.coroutineExceptionHandler.handleException(context, throwable)
			}
		}
	}
}

private external fun setImmediate(callback: () -> Unit)