package online.fivem.common.extensions

import kotlinx.coroutines.launch
import online.fivem.common.common.Event

fun Event.emitAsync(data: Any) {
	launch {
		emit(data)
	}
}