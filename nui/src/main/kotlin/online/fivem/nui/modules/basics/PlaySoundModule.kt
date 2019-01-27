package online.fivem.nui.modules.basics

import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.events.net.PlaySoundEvent
import online.fivem.nui.extensions.nuiResourcesLink
import online.fivem.nui.external.Howl
import online.fivem.nui.external.HowlOptions
import online.fivem.nui.modules.clientEventEchanger.ClientEvent

class PlaySoundModule : AbstractModule() {
	override fun onInit() {
		ClientEvent.on<PlaySoundEvent> { play(it.sound, it.volume) }
	}

	fun play(sound: String, volume: Double) {
		Howl(
			HowlOptions(
				src = arrayOf(Html.nuiResourcesLink("sounds/$sound")),
				volume = volume,
				autoplay = true,
				onend = {

				}
			)
		)
	}
}