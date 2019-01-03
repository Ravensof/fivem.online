package online.fivem.nui.modules.vehicle

import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.events.InternetRadioChangedEvent
import online.fivem.common.events.InternetRadioStopEvent
import online.fivem.common.events.InternetRadioVolumeChangeEvent
import online.fivem.common.extensions.orOne
import online.fivem.nui.extensions.nuiLink
import online.fivem.nui.external.Howl
import online.fivem.nui.external.HowlOptions
import online.fivem.nui.modules.clientEventEchanger.ClientEvent

class InternetRadio : AbstractModule() {

	private var howler: Howl? = null
	private val noisePlayer = Howl(
		HowlOptions(
			src = arrayOf(NOISE_RESOURCE),
			autoplay = false,
			loop = true,
			preload = true
		)
	)
	private var volume: Double = 1.0

	private var internetRadioStation: InternetRadioStation? = null

	override fun start(): Job? {
		ClientEvent.on<InternetRadioChangedEvent> { changeStation(it.internetRadioStation) }

		ClientEvent.on<InternetRadioStopEvent> { disable() }

		ClientEvent.on<InternetRadioVolumeChangeEvent> { changeVolume(it.volume) }

		return super.start()
	}

	override fun stop(): Job? {
		howler?.unload()
		howler = null
		noisePlayer.unload()

		return super.stop()
	}

	private fun changeVolume(volume: Double) {
		this.volume = volume
		howler?.volume(volume * internetRadioStation?.defaultVolume.orOne())

	}

	private fun changeStation(internetRadioStation: InternetRadioStation) {
		this.internetRadioStation = internetRadioStation

		disable()

		val options = HowlOptions(
			src = arrayOf(internetRadioStation.url),
			volume = volume * internetRadioStation.defaultVolume,
			autoplay = true,
			onplay = {
				noisePlayer.stop()
			}
		)

		noisePlayer.apply {
			volume(volume)
			play()
		}

		howler = Howl(options)
	}

	private fun disable() {
		howler?.stop()
		noisePlayer.stop()
	}

	companion object {
		private val NOISE_RESOURCE = Html.nuiLink("radio/noise.mp3")
	}
}