package online.fivem.client.modules.vehicle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.fivem.client.events.PlayerRadioStationChangedEvent
import online.fivem.client.events.ProfileSettingUpdatedEvent
import online.fivem.client.extensions.start
import online.fivem.client.extensions.stop
import online.fivem.client.gtav.Client
import online.fivem.client.modules.nui_event_exchanger.NuiEvent
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Event
import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.events.InternetRadioChangedEvent
import online.fivem.common.events.InternetRadioStopEvent
import online.fivem.common.events.InternetRadioVolumeChangeEvent
import online.fivem.common.extensions.onNull
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeAudioScenes
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation
import kotlin.coroutines.CoroutineContext

class InternetRadio(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private var volume = getSettingsMusicLevel().toDouble() / 10 * MAX_VOLUME

	private val radioStationList = GlobalConfig.internetRadioStations

	override fun onInit() {
		radioStationList.forEach {
			it.value.name?.let { name ->
				Client.addTextEntry(it.key, name)
			}
		}

		Event.apply {
			on<PlayerRadioStationChangedEvent> { onPlayerVehicleRadioStationChanged(it.radioStation) }
			on<ProfileSettingUpdatedEvent.AudioMusicLevelInMP> {
				onSettingsMusicLevelChanged(it.value)
			}
		}
	}

	override fun onStart(): Job? {
		return launch {
			NuiEvent.emit(InternetRadioVolumeChangeEvent(this@InternetRadio.volume))
		}
	}

	override fun onStop(): Job? = launch {
		stopRadio()
	}

	suspend fun playRadio(radio: InternetRadioStation) {
		muteNativeRadio(true)

		NuiEvent.emit(InternetRadioChangedEvent(radio))
	}

	suspend fun stopRadio() {
		muteNativeRadio(false)

		NuiEvent.emit(InternetRadioStopEvent())
	}

	private fun getInternetRadioStation(radioStation: RadioStation?): InternetRadioStation? {
		return radioStation?.let { radioStationList[it.name] }
	}

	private suspend fun onPlayerVehicleRadioStationChanged(radioStation: RadioStation?) {

		val internetRadioStation = getInternetRadioStation(radioStation)

		internetRadioStation?.let {
			playRadio(it)
		}.onNull {
			stopRadio()
		}
	}

	private suspend fun onSettingsMusicLevelChanged(volume: Int) {
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