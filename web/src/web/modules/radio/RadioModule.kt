package web.modules.radio

import external.Howl
import universal.extensions.onNull
import universal.extensions.orOne
import universal.modules.AbstractModule
import universal.modules.radio.InternetRadioStation
import universal.modules.radio.events.RadioChangeStationEvent
import universal.modules.radio.events.RadioChangeVolumeEvent
import universal.modules.radio.events.RadioDisableEvent
import web.common.Event

class RadioModule : AbstractModule() {

	private var howler: Howl? = null
	private var volume: Double = 1.0

	private var internetRadioStation: InternetRadioStation? = null

	init {

		Event.onNui<RadioChangeStationEvent> {
			volume = it.volume
			changeStation(it.internetRadioStation)
		}

		Event.onNui<RadioDisableEvent> {
			disable()
		}

		Event.onNui<RadioChangeVolumeEvent> {
			changeVolume(it.volume)
		}
	}

	private fun changeVolume(volume: Double) {
		this.volume = volume
		howler?.volume(volume * internetRadioStation?.defaultVolume.orOne())
	}

	private fun changeStation(internetRadioStation: InternetRadioStation) {
		this.internetRadioStation = internetRadioStation

		disable()

		val options = object {
			val src = internetRadioStation.url
			val html5 = true // A live stream can only be played through HTML5 Audio.
			val format = arrayOf("opus", "ogg", "mp3")
			val volume = this@RadioModule.volume * internetRadioStation.defaultVolume
		}

		howler = Howl(options).apply {
			play()
		}
	}

	private fun disable() {
		howler?.stop()
		howler = null
	}

	companion object {
		private var instance: RadioModule? = null

		fun getInstance(): RadioModule {
			instance.onNull {
				instance = RadioModule()
			}

			return instance!!
		}
	}
}