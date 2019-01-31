package online.fivem.client.extensions

import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.Sounds
import online.fivem.common.events.nui.PlaySoundEvent
import online.fivem.common.events.nui.PrefetchFileEvent

fun Sounds.play(volume: Double = 1.0) {
	NuiEvent.emit(
		PlaySoundEvent(
			sound = file,
			volume = volume
		)
	)
}

fun Sounds.prefetch() {
	NuiEvent.emit(PrefetchFileEvent(Sounds.PATH + file))
}