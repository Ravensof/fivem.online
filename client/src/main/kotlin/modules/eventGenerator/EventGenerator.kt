package online.fivem.client.modules.eventGenerator

import kotlinx.coroutines.*
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Player
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.entities.Coordinates
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.*
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeControls
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation
import kotlin.js.Date

class EventGenerator : AbstractModule() {

	private lateinit var job50: Job
	private lateinit var job500: Job
	private lateinit var job1000: Job
	private lateinit var jobKeyScan: Job

	private var playerPed = 0
	private var playersVehicle: Int? = null

	private var vehicleBodyHealth: Int? = null
	private var vehicleEngineHealth: Double? = null
	private var vehiclePetrolTankHealth: Double? = null

	private var playerCoordinates: CoordinatesX? = null
	private var playerSeatIndex: Int? = null
	private var pauseMenuState: Int? = null
	private var audioMusicLevelInMP: Int? =
		null// = Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero()
	private var playerRadioStationName: RadioStation? = null //= Player.getRadioStation()
	private var isPlayerVehicleRadioEnabled: Boolean? = null// = playerRadioStationName != null

	override fun start(): Job? {
		TickExecutor.addTick(::checkPressedFlashKeys)

		jobKeyScan = repeatJob(KEY_SCAN_TIME) {
			checkPressedKeys()
		}

		job50 = repeatJob(50) {
			checkVehicleHealth()
		}

		job500 = repeatJob(500) {

			checkPlayerSeatIndex(Client.getPassengerSeatOfPedInVehicle())
			checkPauseMenuState(Client.getPauseMenuState())
			checkIsPlayerInVehicle(Player.isInVehicle())
			checkPlayerRadioStationName(Player.getRadioStation()?.takeIf { playersVehicle != null })
			checkIsPlayerRadioEnabled(playerRadioStationName != null)
		}

		job1000 = repeatJob(1_000) {
			checkPlayersPed(Player.getPed().orZero())

			checkAudioMusicLevelInMP(Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero())
			Client.getEntityCoords(playerPed)?.let { updateCoordinates(it, Client.getEntityHeading(playerPed)) }
		}

		return super.start()
	}

	override fun stop(): Job? {
		jobKeyScan.cancel()
		job1000.cancel()
		job500.cancel()

		return super.stop()
	}

	private fun checkPlayersPed(ped: Int) {
		if (playerPed != ped) {
			playerPed = ped

			UEvent.emit(PlayersPedChangedEvent(ped))
		}
	}

	private fun checkVehicleHealth() {
		if (playersVehicle == null) {
			vehicleBodyHealth = null
			vehicleEngineHealth = null
			vehiclePetrolTankHealth = null
			return
		}

		val vehicle = playersVehicle ?: return

		val bodyHealth = Client.getVehicleBodyHealth(vehicle)
		val engineHealth = Client.getVehicleEngineHealth(vehicle)
		val petrolTankHealth = Client.getVehiclePetrolTankHealth(vehicle)

		if (bodyHealth == vehicleBodyHealth && engineHealth == vehicleEngineHealth && petrolTankHealth == vehiclePetrolTankHealth) return

		UEvent.emit(
			PlayersVehicleHealthChanged(
				bodyHealth = bodyHealth,
				bodyDiff = bodyHealth - (vehicleBodyHealth ?: bodyHealth),

				engineHealth = engineHealth,
				engineDiff = engineHealth - (vehicleEngineHealth ?: engineHealth),

				petrolTankHealth = petrolTankHealth,
				petrolTankDiff = petrolTankHealth - (vehiclePetrolTankHealth ?: petrolTankHealth)
			)
		)

		vehicleBodyHealth = bodyHealth
		vehicleEngineHealth = engineHealth
		vehiclePetrolTankHealth = petrolTankHealth
	}

	private fun updateCoordinates(coordinates: Coordinates, rotation: Float) {
		if (playerCoordinates != coordinates) {
			UEvent.emit(
				PlayerCoordinatesChangedEvent(
					CoordinatesX(coordinates, rotation)
				)
			)
		}
	}

	private fun repeatJob(timeMillis: Long, function: () -> Unit): Job = GlobalScope.launch {
		var startTime: Double
		var endTime: Double

		while (this.isActive) {
			startTime = Date.now()
			function()

			endTime = Date.now()
			if (endTime - startTime < timeMillis) {
				delay((timeMillis - endTime + startTime).toLong())
			}
		}
	}

	private fun checkPressedKeys() {
		val group = NativeControls.Groups.MOVE

		pressedKeys.forEachIndexed { index, pair ->

			val isControlPressed =
				Client.isControlPressed(group, pair.first) || Client.isDisabledControlPressed(group, pair.first)

			if (isControlPressed) {

				if (pair.second == 0.0) {
					UEvent.emit(ControlJustPressedEvent(pair.first))

					pressedKeys[index] = pair.first to Date.now()
				} else if (pair.second > 0 && Date.now() - pair.second > KEY_HOLD_TIME) {
					UEvent.emit(ControlLongPressedEvent(pair.first))

					pressedKeys[index] = pair.first to -1.0
				}

			} else {

				if (pair.second != 0.0) {
					if (pair.second != -1.0) {
						UEvent.emit(ControlShortPressedEvent(pair.first))
					} else {
						UEvent.emit(ControlJustReleasedEvent(pair.first))
					}

					pressedKeys[index] = pair.first to 0.0
				}
			}
		}
	}

	private fun checkPressedFlashKeys() {
		val group = NativeControls.Groups.MOVE

		pressedFlashKeys.forEachIndexed { index, pair ->
			val isControlJustPressed =
				Client.isControlJustPressed(group, pair.first) || Client.isDisabledControlJustPressed(group, pair.first)

			if (pair.second != 0.0) {
				if (Date.now() - pair.second >= KEY_DEBOUNCE_TIME) {
					pressedFlashKeys[index] = pair.first to 0.0
				} else {
					return@forEachIndexed
				}
			}

			if (isControlJustPressed) {
				pressedFlashKeys[index] = pair.first to Date.now()
				UEvent.emit(ControlShortPressedEvent(pair.first))
			}
		}
	}

	private fun checkPlayerSeatIndex(seatIndex: Int?) {
		if (seatIndex != playerSeatIndex) {
			when (seatIndex) {
				-1 -> UEvent.emit(PlayerGetInDriversSeatEvent())

				null -> {
				}//UEvent.emit(PlayerSeatChangedEvent(seatIndex))

				else -> UEvent.emit(PlayerGetInPassengerSeatEvent(seatIndex))
			}

			playerSeatIndex = seatIndex
		}
	}

	private fun checkPauseMenuState(state: Int) {
		if (pauseMenuState != state) {
			UEvent.emit(PauseMenuStateChangedEvent(state))

			pauseMenuState = state
		}
	}

	private fun checkAudioMusicLevelInMP(volume: Int) {
		if (audioMusicLevelInMP != volume) {
			UEvent.emit(AudioMusicLevelInMPChangedEvent(volume))

			audioMusicLevelInMP = volume
		}
	}

	private fun checkIsPlayerInVehicle(isInVehicle: Boolean) {
		val currentVehicle = if (isInVehicle) Client.getVehiclePedIsUsing(playerPed) else null

		if (currentVehicle != playersVehicle) {
			playersVehicle = currentVehicle

			UEvent.emit(PlayersVehicleChanged(currentVehicle))

			if (currentVehicle != null) {
				UEvent.emit(
					PlayerJoinVehicleEvent(
						currentVehicle.orZero(),
						Client.getPassengerSeatOfPedInVehicle().orZero()
					)
				)
			} else {
				UEvent.emit(PlayerLeftVehicleEvent())
			}
		}
	}

	private fun checkPlayerRadioStationName(radioStation: RadioStation?) {
		if (radioStation != playerRadioStationName) {

			UEvent.emit(PlayerRadioStationChangedEvent(radioStation))

			playerRadioStationName = radioStation
		}
	}

	private fun checkIsPlayerRadioEnabled(enabled: Boolean) {
		if (enabled != isPlayerVehicleRadioEnabled) {
			UEvent.emit(PlayerVehicleRadioToggledEvent(enabled))

			if (enabled) {
				UEvent.emit(PlayerVehicleRadioEnabledEvent())
			} else {
				UEvent.emit(PlayerVehicleRadioDisabledEvent())
			}

			isPlayerVehicleRadioEnabled = enabled
		}
	}

	companion object {

		private val flashKeys = arrayListOf(
			NativeControls.Keys.CELLPHONE_SCROLL_BACKWARD,
			NativeControls.Keys.CURSOR_SCROLL_UP,
			NativeControls.Keys.CELLPHONE_SCROLL_FORWARD,
			NativeControls.Keys.CURSOR_SCROLL_DOWN,
			NativeControls.Keys.PREV_WEAPON,
			NativeControls.Keys.NEXT_WEAPON,
			NativeControls.Keys.VEH_SLOWMO_UD,
			NativeControls.Keys.VEH_SLOWMO_UP_ONLY,
			NativeControls.Keys.VEH_SLOWMO_DOWN_ONLY
		)

		private const val KEY_HOLD_TIME = 250
		private const val KEY_DEBOUNCE_TIME = 75
		private const val KEY_SCAN_TIME = 40L

		fun addListenedKey(control: NativeControls.Keys) {

			if (isFlashKey(control)) {
				pressedFlashKeys.forEach {
					if (it.first == control) return
				}

				pressedFlashKeys.add(control to 0.0)

				return
			}

			pressedKeys.forEach {
				if (it.first == control) return
			}

			pressedKeys.add(control to 0.0)
		}

		fun isFlashKey(control: NativeControls.Keys): Boolean {
			return flashKeys.contains(control)
		}

		private val pressedKeys: MutableList<Pair<NativeControls.Keys, Double>> = mutableListOf()
		private val pressedFlashKeys: MutableList<Pair<NativeControls.Keys, Double>> = mutableListOf()
	}
}