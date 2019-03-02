package online.fivem.nui.modules.basics

import external.howler.Howl
import external.howler.HowlOptions
import online.fivem.common.Sounds
import online.fivem.common.common.Html
import online.fivem.common.events.nui.PlaySoundEvent
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.extensions.nuiResourcesLink
import online.fivem.nui.modules.client_event_exchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class PlaySoundModule(override val coroutineContext: CoroutineContext) : AbstractNuiModule() {
	override fun onInit() {
		ClientEvent.on<PlaySoundEvent> { play(it.sound, it.volume) }
	}

	fun play(sound: String, volume: Double) {
		Howl(
			HowlOptions(
				src = arrayOf(Html.nuiResourcesLink(Sounds.PATH + sound)),
				volume = volume,
				autoplay = true,
				onend = {

				}
			)
		)
	}
}