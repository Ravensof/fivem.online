package online.fivem.client.modules.vehicle

import kotlinx.coroutines.Job
import online.fivem.client.extensions.start
import online.fivem.client.extensions.stop
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.events.InternetRadioChangedEvent
import online.fivem.common.events.InternetRadioStopEvent
import online.fivem.common.events.InternetRadioVolumeChangeEvent
import online.fivem.common.events.local.PlayerRadioStationChangedEvent
import online.fivem.common.events.local.ProfileSettingUpdatedEvent
import online.fivem.common.extensions.onNull
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeAudioScenes
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation

class InternetRadio : AbstractModule() {

	private var volume = getSettingsMusicLevel().toDouble() / 10 * MAX_VOLUME

	private val radioStationList = GlobalConfig.internetRadioStations

	override fun onInit() {
		radioStationList.forEach {
			it.value.name?.let { name ->
				Client.addTextEntry(it.key, name)
			}
		}

		UEvent.on<PlayerRadioStationChangedEvent> { onPlayerVehicleRadioStationChanged(it.radioStation) }

		UEvent.on<ProfileSettingUpdatedEvent.AudioMusicLevelInMP> {
			onSettingsMusicLevelChanged(it.value)
		}
	}

	override fun onStart(): Job? {
		NuiEvent.emit(InternetRadioVolumeChangeEvent(this@InternetRadio.volume))

		return super.onStart()
	}

	fun playRadio(radio: InternetRadioStation) {
		muteNativeRadio(true)

		NuiEvent.emit(InternetRadioChangedEvent(radio))
	}

	fun stopRadio() {
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
			NativeAudioScenes.DLC_MPHEIST_TRANSITION_TO_APT_FADE_IN_RADIO_SCENE.start()
		} else {
			NativeAudioScenes.DLC_MPHEIST_TRANSITION_TO_APT_FADE_IN_RADIO_SCENE.stop()
		}
	}

	companion object {
		const val MAX_VOLUME = 0.3
	}
}