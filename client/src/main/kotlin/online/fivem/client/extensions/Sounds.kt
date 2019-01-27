package online.fivem.client.extensions

import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.Sounds
import online.fivem.common.events.net.PlaySoundEvent

fun Sounds.play(volume: Double = 1.0) {
	NuiEvent.emit(
		PlaySoundEvent(
			sound = file,
			volume = volume
		)
	)
}