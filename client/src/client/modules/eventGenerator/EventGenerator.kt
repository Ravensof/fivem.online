package client.modules.eventGenerator

import client.common.Client
import client.common.Player
import client.extensions.orZero
import client.modules.eventGenerator.events.vehicle.PlayerJoinVehicle
import client.modules.eventGenerator.events.vehicle.PlayerLeftOrJoinVehicle
import client.modules.eventGenerator.events.vehicle.PlayerLeftVehicle
import client.modules.eventGenerator.events.vehicle.radio.*
import universal.common.Event
import universal.common.setInterval
import universal.extensions.onNull
import universal.r.ProfileSetting
import universal.r.RadioStation

class EventGenerator {

	init {
		setInterval(500) {
			checkIsPlayerInVehicle(Player.isInVehicle())
			checkPlayerRadioStationName(Player.getRadioStation()?.takeIf { isPlayerInVehicle })
			checkIsPlayerRadioEnabled(playerRadioStationName != null)

		}

		setInterval(1000) {
			checkAudioMusicLevelInMP(Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero())
		}
	}

	private var audioMusicLevelInMP = Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero()

	private fun checkAudioMusicLevelInMP(volume: Int) {
		if (audioMusicLevelInMP != volume) {
			Event.emit(AudioMusicLevelInMPChanged(volume))

			audioMusicLevelInMP = volume
		}
	}

	private var isPlayerInVehicle = Player.isInVehicle()

	private fun checkIsPlayerInVehicle(isInVehicle: Boolean) {
		if (isPlayerInVehicle != isInVehicle) {
			if (isInVehicle) {
				Event.emit(PlayerJoinVehicle())
			} else {
				Event.emit(PlayerLeftVehicle())
			}

			Event.emit(PlayerLeftOrJoinVehicle())

			isPlayerInVehicle = isInVehicle
		}
	}

	private var playerRadioStationName = Player.getRadioStation()

	private fun checkPlayerRadioStationName(radioStation: RadioStation?) {
		if (radioStation != playerRadioStationName) {

			Event.emit(PlayerRadioStationChanged(radioStation))

			playerRadioStationName = radioStation
		}
	}

	private var isPlayerVehicleRadioEnabled = playerRadioStationName != null

	private fun checkIsPlayerRadioEnabled(enabled: Boolean) {
		if (enabled != isPlayerVehicleRadioEnabled) {
			if (enabled) {
				Event.emit(PlayerVehicleRadioEnabled())
			} else {
				Event.emit(PlayerVehicleRadioDisabled())
			}

			Event.emit(PlayerVehicleRadioToggled(enabled))

			isPlayerVehicleRadioEnabled = enabled
		}
	}

	companion object {

		private var instance: EventGenerator? = null

		fun getInstance(): EventGenerator {
			instance.onNull {
				instance = EventGenerator()
			}

			return instance!!
		}
	}
}