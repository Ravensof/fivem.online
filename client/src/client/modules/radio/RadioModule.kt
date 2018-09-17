package client.modules.radio

import client.common.Client
import client.common.Player
import client.extensions.emitNui
import client.extensions.onNui
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

	private val radioStationList = RadioStationList

	private var currentRadio: InternetRadioStation? = getInternetRadioStation(Player.getRadioStation())

	private var isPlaying: Boolean = false
		get() {
			return currentRadio != null
		}

	init {

		Event.on { it: PlayerRadioStationChanged ->
			onPlayerVehicleRadioStationChanged(it.radioStation)
		}

		Event.on { event: AudioMusicLevelInMPChanged ->
			onSettingsMusicLevelChanged(event.volume)
		}

		Event.onNui("radio:ready") { _: Any, _: (String) -> Unit ->
			Console.info("on ready received")
			onReady()
//			callback("custom text")
		}

		onReady()

		Console.info("RadioModule loaded")
	}

	fun playCustomRadio(radio: InternetRadioStation) {
		currentRadio = radio
		toggleCustomRadioBehavior()

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

	private fun getInternetRadioStation(radioStation: RadioStation?): InternetRadioStation? {
		return radioStation?.let { radioStationList[it.name] }
	}

	private fun onReady() {
		currentRadio?.let {
			playCustomRadio(it)
		}
	}

	private fun onPlayerVehicleRadioStationChanged(radioStation: RadioStation?) {

		val internetRadioStation = getInternetRadioStation(radioStation)

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



