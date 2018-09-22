package client.modules.session.extensions

import client.modules.session.SessionModule
import fivem.common.Exports
import universal.common.Console
import universal.common.Event
import universal.common.normalizeEventName

fun Event.emitSafeNet(data: Any) {
	SessionModule.token?.let { token ->
		Console.debug("safe net event " + normalizeEventName(data::class.toString()) + " sent")
		Exports.emitNet(
				SAFE_EVENT_PREFIX + normalizeEventName(data::class.toString()),
				token,
				data
		)
		Unit
	}
}