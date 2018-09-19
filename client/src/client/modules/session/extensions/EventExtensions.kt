package client.modules.session.extensions

import client.modules.session.SessionModule
import shared.common.Console
import shared.common.Event
import shared.common.Exports
import shared.normalizeEventName

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