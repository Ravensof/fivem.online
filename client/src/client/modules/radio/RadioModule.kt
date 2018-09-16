package client.modules.radio

import client.common.Client
import client.extensions.emitNui
import client.extensions.orZero
import client.modules.AbstractModule
import client.modules.eventGenerator.events.AudioMusicLevelInMPChanged
import client.modules.eventGenerator.events.PlayerRadioStationChanged
import client.modules.radio.data.RadioStationList
import shared.common.Console
import shared.common.Event
import shared.modules.radio.InternetRadioStation
import shared.r.ProfileSetting
import shared.r.RadioStation


class RadioModule : AbstractModule() {

	private var volume = getSettingsMusicLevel() / 10 * MAX_VOLUME

	private var currentRadio: InternetRadioStation? = null

	private var isPlaying: Boolean = false
		get() {
			return currentRadio != null
		}

	private val radioStationList = RadioStationList

	init {

		Event.on { it: PlayerRadioStationChanged ->
			onPlayerVehicleRadioStationChanged(it.radioStation)
		}

		Event.on { event: AudioMusicLevelInMPChanged ->
			onSettingsMusicLevelChanged(event.volume)
		}

//		Event.onNui("radio:ready") { data: Any, cb: Any ->//todo проверить
//			Console.info(data)
//			Console.info(cb)
////			Event.emitNui(arrayOf(
////					"type" to "create",
////					"radios" to customRadios,
////					"volume" to volume
////			))
//		}

//		Event.emitNui(arrayOf(
//				"type" to "create",
//				"radios" to customRadios,
//				"volume" to volume
//		))

//		Event.emitNui(arrayOf(
//				"type" to "play"
//		))

	}

	fun playCustomRadio(radio: InternetRadioStation) {
		currentRadio = radio
		toggleCustomRadioBehavior()
		Console.info(volume)
		Event.emitNui(object {
			val type = "play"
			val url = radio.url
			val volume = this@RadioModule.volume * radio.defaultVolume
		})
	}

	fun stopCustomRadio() {
		currentRadio = null
		toggleCustomRadioBehavior()

		Event.emitNui(object {
			val type = "stop"
		})
	}

	private fun onPlayerVehicleRadioStationChanged(radioStation: RadioStation?) {

		val internetRadioStation = radioStation?.let { radioStationList.get(it.name) }

		if (internetRadioStation != null)
			playCustomRadio(internetRadioStation)
		else
			stopCustomRadio()

	}

	private fun onSettingsMusicLevelChanged(volume: Int) {
		this.volume = volume / 10 * MAX_VOLUME

		currentRadio?.let {
			Event.emitNui(object {
				val type = "volume"
				val volume = this@RadioModule.volume * it.defaultVolume
			})
		}
	}

	private fun getSettingsMusicLevel(): Int {
		return Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero()
	}

	private fun toggleCustomRadioBehavior() {
		Client.setFrontendRadioActive(!isPlaying)

		if (isPlaying) {
			Client.startAudioScene("DLC_MPHEIST_TRANSITION_TO_APT_FADE_IN_RADIO_SCENE")
		} else {
			Client.stopAudioScene("DLC_MPHEIST_TRANSITION_TO_APT_FADE_IN_RADIO_SCENE")
		}
	}

	companion object {
		const val MAX_VOLUME = 0.3
	}
}



