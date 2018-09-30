package client.modules.radio

import client.common.Client
import client.common.Player
import client.extensions.emitNui
import client.extensions.orZero
import client.modules.AbstractModule
import client.modules.eventGenerator.events.vehicle.radio.AudioMusicLevelInMPChangedEvent
import client.modules.eventGenerator.events.vehicle.radio.PlayerRadioStationChangedEvent
import client.modules.radio.data.RadioStationList
import universal.common.Console
import universal.common.Event
import universal.extensions.onNull
import universal.modules.radio.InternetRadioStation
import universal.modules.radio.events.RadioChangeStationEvent
import universal.modules.radio.events.RadioChangeVolumeEvent
import universal.modules.radio.events.RadioDisableEvent
import universal.r.ProfileSetting
import universal.r.RadioStation


class RadioModule private constructor() : AbstractModule() {

	private var volume = getSettingsMusicLevel().toDouble() / 10 * MAX_VOLUME

	private val radioStationList = RadioStationList

	private var currentRadio: InternetRadioStation? = getInternetRadioStation(Player.getRadioStation())

	private var isPlaying: Boolean = false
		get() {
			return currentRadio != null
		}

	init {

		Event.on { it: PlayerRadioStationChangedEvent ->
			onPlayerVehicleRadioStationChanged(it.radioStation)
		}

		Event.on { event: AudioMusicLevelInMPChangedEvent ->
			onSettingsMusicLevelChanged(event.volume)
		}

		onReady()

		Console.info("RadioModule loaded")
	}

	fun playCustomRadio(radio: InternetRadioStation) {
		currentRadio = radio
		toggleCustomRadioBehavior()

		Event.emitNui(RadioChangeStationEvent(radio, volume))
	}

	fun stopCustomRadio() {
		currentRadio = null
		toggleCustomRadioBehavior()

		Event.emitNui(RadioDisableEvent())
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

		internetRadioStation?.let {
			playCustomRadio(it)
		}.onNull {
			stopCustomRadio()
		}
	}

	private fun onSettingsMusicLevelChanged(volume: Int) {
		this.volume = volume.toDouble() / 10 * MAX_VOLUME

		currentRadio?.let {
			Event.emitNui(RadioChangeVolumeEvent(this@RadioModule.volume))
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

		private var instance: RadioModule? = null

		fun getInstance(): RadioModule {
			instance.onNull {
				instance = RadioModule()
			}

			return instance!!
		}
	}
}



