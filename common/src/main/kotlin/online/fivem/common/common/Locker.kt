package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Locker : CoroutineScope {
	override val coroutineContext: CoroutineContext = createSupervisorJob()

	private val lockQueue = mutableSetOf<Any>()
	private var lockKey: Any? = null

	private val unlockChannel = Channel<Unit>()

	init {
		unlock()
	}

	suspend fun lock(key: Any) {
		if (lockQueue.contains(key)) return
		lockQueue.add(key)

		unlockChannel.receive()
		if (!lockQueue.contains(key)) return//throw IllegalStateException("locking is been canceled ($key)")

		lockKey = key
		lockQueue.remove(key)
	}

	fun unlock(key: Any) {
		lockQueue.remove(key)

		if (lockKey != key) return

		lockKey = null

		unlock()
	}

	fun isLocked() = lockKey != null

	private fun unlock() = launch {
		unlockChannel.send(Unit)
	}
}
