package online.fivem.common.extensions

import kotlinx.coroutines.Job

suspend fun Job.cancelAndJoin() {
	cancel()
	join()
}