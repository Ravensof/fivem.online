package online.fivem.client.modules.vehicle

import kotlinx.coroutines.Job
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Player
import online.fivem.client.modules.nuiEventExchanger.NuiEvent
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.events.*
import online.fivem.common.extensions.onNull
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeScenes
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation

class InternetRadio : AbstractModule() {

	private var volume = getSettingsMusicLevel().toDouble() / 10 * MAX_VOLUME

	private val radioStationList = GlobalConfig.internetRadioStations

	private var currentRadio: InternetRadioStation? = getInternetRadioStation(Player.getRadioStation())

	override fun start(): Job? {
		UEvent.on<PlayerRadioStationChangedEvent> {
			onPlayerVehicleRadioStationChanged(it.radioStation)
		}

		UEvent.on<AudioMusicLevelInMPChangedEvent> {
			onSettingsMusicLevelChanged(it.value)
		}

		NuiEvent.emit(InternetRadioVolumeChangeEvent(this@InternetRadio.volume))

		currentRadio?.let { playRadio(it) }

		return super.start()
	}

	fun playRadio(radio: InternetRadioStation) {
		currentRadio = radio
		muteNativeRadio(true)

		NuiEvent.emit(InternetRadioChangedEvent(radio))
	}

	fun stopRadio() {
		currentRadio = null
		muteNativeRadio(false)

		NuiEvent.emit(InternetRadioStopEvent())
	}

	private fun getInternetRadioStation(radioStation: RadioStation?): InternetRadioStation? {
		return radioStation?.let { radioStationList[it.name] }
	}

	private fun onPlayerVehicleRadioStationChanged(radioStation: RadioStation?) {

		val internetRadioStation = getInternetRadioStation(radioStation)

		internetRadioStation?.let {
			playRadio(it)
		}.onNull {
			stopRadio()
		}
	}

	private fun onSettingsMusicLevelChanged(volume: Int) {
		this.volume = volume.toDouble() / 10 * MAX_VOLUME

		NuiEvent.emit(InternetRadioVolumeChangeEvent(this@InternetRadio.volume))
	}

	private fun getSettingsMusicLevel(): Int {
		return Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero()
	}

	private fun muteNativeRadio(mute: Boolean) {
		Client.setFrontendRadioActive(!mute)

		if (mute) {
			Client.startAudioScene(NativeScenes.DLC_MPHEIST_TRANSITION_TO_APT_FADE_IN_RADIO_SCENE.name)
		} else {
			Client.stopAudioScene(NativeScenes.DLC_MPHEIST_TRANSITION_TO_APT_FADE_IN_RADIO_SCENE.name)
		}
	}

	companion object {
		const val MAX_VOLUME = 0.3
	}
}