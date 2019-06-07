package online.fivem.common.common

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BufferedAction {

	private val locker = Locker()

	private var queueCounter = 0

	private var action: Deferred<Unit>? = null
	private var isInProgress = false

	suspend fun start(key: Any, action: suspend () -> Unit) {
		queueCounter++
		locker.lock(key)
		queueCounter--

		if (isInProgress) return
		isInProgress = true
		this.action = GlobalScope.async { action() }
		this.action?.await()
	}

	suspend fun cancel(key: Any, undoAction: suspend () -> Unit) {
		if (queueCounter == 0) {
			action?.await()
			undoAction()
			isInProgress = false
		}

		locker.unlock(key)
	}
}