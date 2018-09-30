package client.modules.eventGenerator

import client.common.Client
import client.common.Player
import client.extensions.orZero
import client.modules.eventGenerator.events.PauseMenuStateChangedEvent
import client.modules.eventGenerator.events.controls.ControlJustPressedEvent
import client.modules.eventGenerator.events.controls.ControlJustReleasedEvent
import client.modules.eventGenerator.events.controls.ControlLongPressedEvent
import client.modules.eventGenerator.events.vehicle.PlayerJoinVehicleEvent
import client.modules.eventGenerator.events.vehicle.PlayerLeftOrJoinVehicleEvent
import client.modules.eventGenerator.events.vehicle.PlayerLeftVehicleEvent
import client.modules.eventGenerator.events.vehicle.PlayerSeatChangedEvent
import client.modules.eventGenerator.events.vehicle.radio.*
import universal.common.Event
import universal.common.setInterval
import universal.extensions.onNull
import universal.r.Controls
import universal.r.ProfileSetting
import universal.r.RadioStation
import kotlin.js.Date

class EventGenerator {

	init {

		setInterval(40) {
			checkPressedKeys()
		}

		setInterval(500) {

			checkPlayerSeatIndex(Client.getPassengerSeatOfPedInVehicle())
			checkPauseMenuState(Client.getPauseMenuState())
			checkIsPlayerInVehicle(Player.isInVehicle())
			checkPlayerRadioStationName(Player.getRadioStation()?.takeIf { isPlayerInVehicle == true })
			checkIsPlayerRadioEnabled(playerRadioStationName != null)
		}

		setInterval(1000) {
			checkAudioMusicLevelInMP(Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero())
		}
	}

	private fun checkPressedKeys() {
		val group = Controls.Groups.INPUTGROUP_LOOK

		pressedKeys.forEachIndexed { index, pair ->

			if (Client.isControlPressed(group, pair.first)) {

				if (pair.second == 0.0) {
					Event.emit(ControlJustPressedEvent(pair.first))
					pressedKeys[index] = pair.first to Date.now()
				} else if (pair.second > 0 && Date.now() - pair.second > KEY_HOLD_TIME) {
					Event.emit(ControlLongPressedEvent(pair.first))
					pressedKeys[index] = pair.first to -1.0
				}
			} else {

				if (pair.second != 0.0) {
					Event.emit(ControlJustReleasedEvent(pair.first))
					pressedKeys[index] = pair.first to 0.0
				}
			}
		}
	}

	private var playerSeatIndex: Int? = null

	private fun checkPlayerSeatIndex(seatIndex: Int?) {
		if (seatIndex != playerSeatIndex) {
			Event.emit(PlayerSeatChangedEvent(seatIndex))

			playerSeatIndex = seatIndex
		}
	}

	private var pauseMenuState: Int? = null

	private fun checkPauseMenuState(state: Int) {
		if (pauseMenuState != state) {
			Event.emit(PauseMenuStateChangedEvent(state))

			pauseMenuState = state
		}
	}

	private var audioMusicLevelInMP: Int? = null// = Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero()

	private fun checkAudioMusicLevelInMP(volume: Int) {
		if (audioMusicLevelInMP != volume) {
			Event.emit(AudioMusicLevelInMPChangedEvent(volume))

			audioMusicLevelInMP = volume
		}
	}

	private var isPlayerInVehicle: Boolean? = null// = Player.isInVehicle()

	private fun checkIsPlayerInVehicle(isInVehicle: Boolean) {
		if (isPlayerInVehicle != isInVehicle) {
			if (isInVehicle) {
				Event.emit(PlayerJoinVehicleEvent(Client.getPassengerSeatOfPedInVehicle().orZero()))
			} else {
				Event.emit(PlayerLeftVehicleEvent())
			}

			Event.emit(PlayerLeftOrJoinVehicleEvent())

			isPlayerInVehicle = isInVehicle
		}
	}

	private var playerRadioStationName: RadioStation? = null //= Player.getRadioStation()

	private fun checkPlayerRadioStationName(radioStation: RadioStation?) {
		if (radioStation != playerRadioStationName) {

			Event.emit(PlayerRadioStationChangedEvent(radioStation))

			playerRadioStationName = radioStation
		}
	}

	private var isPlayerVehicleRadioEnabled: Boolean? = null// = playerRadioStationName != null

	private fun checkIsPlayerRadioEnabled(enabled: Boolean) {
		if (enabled != isPlayerVehicleRadioEnabled) {
			if (enabled) {
				Event.emit(PlayerVehicleRadioEnabledEvent())
			} else {
				Event.emit(PlayerVehicleRadioDisabledEvent())
			}

			Event.emit(PlayerVehicleRadioToggledEvent(enabled))

			isPlayerVehicleRadioEnabled = enabled
		}
	}

	companion object {

		private const val KEY_HOLD_TIME = 250

		private var instance: EventGenerator? = null

		fun getInstance(): EventGenerator {
			instance.onNull {
				instance = EventGenerator()
			}

			return instance!!
		}

		fun addListenedKey(control: Controls.Keys) {

			pressedKeys.forEach {
				if (it.first == control) {
					return
				}
			}

			pressedKeys.add(control to 0.0)
		}

		private val pressedKeys: MutableList<Pair<Controls.Keys, Double>> = mutableListOf()
	}
}