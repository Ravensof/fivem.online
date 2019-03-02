package online.fivem.client.events

import kotlinx.coroutines.Deferred
import online.fivem.common.common.AbstractProcessEvent

class PlayerSpawnProcess(
	event: Deferred<FinishedEvent>,
	onComplete: (FinishedEvent) -> Unit

) : AbstractProcessEvent<PlayerSpawnProcess.FinishedEvent>(event, onComplete) {

	class FinishedEvent
}