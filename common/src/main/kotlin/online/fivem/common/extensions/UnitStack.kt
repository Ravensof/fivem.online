package online.fivem.common.extensions

import online.fivem.common.common.Handle
import online.fivem.common.common.Stack

typealias UnitStack = Stack<Unit>

fun UnitStack.set(doOnce: () -> Unit): Handle {
	if (isEmpty()) {
		doOnce()
	}
	return add(Unit)
}

fun UnitStack.unset(handle: Handle, doOnce: () -> Unit) {
	remove(handle)
	if (isEmpty()) {
		doOnce()
	}
}

//fun UnitStack.setJob(doOnce: suspend () -> Unit): Deferred<Handle> = async {
//	if (isEmpty()) {
//		doOnce()
//	}
//	return@async add(Unit)
//}
//
//fun UnitStack.unsetJob(handle: Handle, doOnce: suspend () -> Unit): Job {
//	remove(handle)
//	return launch {
//		if (isEmpty()) {
//			doOnce()
//		}
//	}
//}