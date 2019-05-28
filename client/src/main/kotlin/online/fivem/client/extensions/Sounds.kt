package online.fivem.client.extensions

import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.Sounds
import online.fivem.common.events.net.AcceptEvent
import online.fivem.common.events.nui.PlaySoundEvent
import online.fivem.common.events.nui.PrefetchFileEvent
import online.fivem.common.extensions.forEach

suspend fun Sounds.play(volume: Double = 1.0) {
	val event = PlaySoundEvent(
		sound = file,
		volume = volume
	)

	val channel = NuiEvent.openSubscription(AcceptEvent::class)

	NuiEvent.emit(event)

	channel.forEach {
		if (it.eventId == event.id) channel.cancel()
	}
}

suspend fun Sounds.prefetch() {
	NuiEvent.emit(PrefetchFileEvent(Sounds.PATH + file))
}