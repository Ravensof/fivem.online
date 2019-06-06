package online.fivem.client.modules.vehicle

import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.events.PlayerRadioStationChangedEvent
import online.fivem.client.events.ProfileSettingUpdatedEvent
import online.fivem.client.extensions.start
import online.fivem.client.extensions.stop
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Event
import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.events.nui.InternetRadioModuleEvent
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeAudioScenes
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation

class InternetRadioModule : AbstractClientModule() {

	private var volume = getSettingsMusicLevel().toDouble() / 10 * MAX_VOLUME

	private val radioStationList = GlobalConfig.internetRadioStations

	override suspend fun onInit() {
		radioStationList.forEach {
			it.value.name?.let { name ->
				Client.addTextEntry(it.key, name)
			}
		}

		Event.on<PlayerRadioStationChangedEvent> {
			onPlayerVehicleRadioStationChanged(it.radioStation)
		}

		Event.on<ProfileSettingUpdatedEvent.AudioMusicLevelInMP> {
			onSettingsMusicLevelChanged(it.value)
		}
	}

	override fun onStart() = launch {
		NuiEvent.emit(InternetRadioModuleEvent.VolumeChanged(this@InternetRadioModule.volume))
	}

	override fun onStop() = launch {
		stopRadio()
	}

	suspend fun playRadio(radio: InternetRadioStation) {
		muteNativeRadio(true)

		NuiEvent.emit(InternetRadioModuleEvent.Changed(radio))
	}

	suspend fun stopRadio() {
		muteNativeRadio(false)

		NuiEvent.emit(InternetRadioModuleEvent.Stop())
	}

	private fun getInternetRadioStation(radioStation: RadioStation?): InternetRadioStation? {
		return radioStation?.let { radioStationList[it.name] }
	}

	private fun onPlayerVehicleRadioStationChanged(radioStation: RadioStation?) = launch {

		val internetRadioStation = getInternetRadioStation(radioStation)

		internetRadioStation?.let {
			return@launch playRadio(it)
		}

		stopRadio()
	}

	private fun onSettingsMusicLevelChanged(volume: Int) = launch {
		this@InternetRadioModule.volume = volume.toDouble() / 10 * MAX_VOLUME

		NuiEvent.emit(InternetRadioModuleEvent.VolumeChanged(this@InternetRadioModule.volume))
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