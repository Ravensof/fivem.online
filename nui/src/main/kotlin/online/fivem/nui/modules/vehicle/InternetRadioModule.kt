package online.fivem.nui.modules.vehicle

import external.howler.Howl
import external.howler.HowlOptions
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.common.Html
import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.events.nui.InternetRadioModuleEvent
import online.fivem.common.extensions.orOne
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.extensions.nuiResourcesLink
import online.fivem.nui.modules.client_event_exchanger.ClientEvent

class InternetRadioModule : AbstractNuiModule() {

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

	override fun onStartAsync(): Deferred<*>? {
		ClientEvent.apply {
			on<InternetRadioModuleEvent.Changed> { changeStation(it.internetRadioStation) }
			on<InternetRadioModuleEvent.Stop> { disable() }
			on<InternetRadioModuleEvent.VolumeChanged> { changeVolume(it.volume) }
		}

		return super.onStartAsync()
	}

	override fun onStop(): Job? {
		howler?.unload()
		howler = null
		noisePlayer.unload()

		return super.onStop()
	}

	private fun changeVolume(volume: Double) {
		this.volume = volume
		howler?.volume(volume * internetRadioStation?.defaultVolume.orOne())

	}

	private fun changeStation(internetRadioStation: InternetRadioStation) {
		this.internetRadioStation = internetRadioStation

		disable()

		noisePlayer.apply {
			volume(volume)
			play()
		}

		var options: HowlOptions? = null
		var attemptsLeft = MAX_RECONNECTING_ATTEMPTS

		options = HowlOptions(
			src = arrayOf(internetRadioStation.url),
			volume = volume * internetRadioStation.defaultVolume,
			autoplay = true,
			onplay = {
				noisePlayer.stop()
				options = null
			},
			onloaderror = {
				if (attemptsLeft-- > 0) {
					howler?.unload()
					launch {
						delay(250)
						options?.let { howler = Howl(it) }
					}
				}
			}
		).also {
			howler = Howl(it)
		}
	}

	private fun disable() {
		howler?.unload()
		noisePlayer.stop()
	}

	companion object {
		private val NOISE_RESOURCE = Html.nuiResourcesLink("modules/radio/noise.mp3")
		private const val MAX_RECONNECTING_ATTEMPTS = 500
	}
}