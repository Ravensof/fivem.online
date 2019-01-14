package online.fivem.client.modules.eventGenerator

import kotlinx.coroutines.*
import online.fivem.client.extensions.getPassengerSeatOfPedInVehicle
import online.fivem.client.extensions.isPedAtGetInAnyVehicle
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.*
import online.fivem.common.extensions.orZero
import online.fivem.common.gtav.NativeControls
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class EventGeneratorModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	private var playerPed = 0
	private var playerPedHealth: Int = 0
	private var playersVehicle: Int? = null

	private var vehicleBodyHealth: Int? = null
	private var vehicleEngineHealth: Double? = null
	private var vehiclePetrolTankHealth: Double? = null
	private var isFadeOut: Boolean = true

	private var playerCoordinates: CoordinatesX? = null
	private var playerSeatIndex: Int? = null
	private var pauseMenuState: Int? = null
	private var audioMusicLevelInMP: Int? = null
	private var playerRadioStationName: RadioStation? = null

	var accelerationThreshold: Double = 100.0
		set(value) {
			if (value > field) {
				field = value
			}
		}
	private var iPlayerSpeed = 0.0
	private var iPlayerAcceleration = 0.0
	private var iPlayerAccelerationModule = 0.0
	private var iLastSpeedCheck = 0.0
	private var isAccelerationThresholdAcchieved = false

	override fun start(): Job? {
		TickExecutor.addTick(::checkPressedFlashKeys)

		repeatJob(KEY_SCAN_TIME) {
			checkPressedKeys()
		}

		repeatJob(50) {
			checkVehicleHealth()
			checkAcceleration()
		}

		repeatJob(500) {
			checkPlayerSeatIndex(Client.getPassengerSeatOfPedInVehicle())
			checkPauseMenuState(Client.getPauseMenuState())
			checkIsPlayerInVehicle()
			checkRadio()
		}

		repeatJob(1_000) {
			checkPlayersPed(Client.getPlayerPed())

			checkAudioMusicLevelInMP(Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero())
			checkFadeInOut(Client.isScreenFadedOut())
		}

		return super.start()
	}

	override fun stop(): Job? {
		cancel()

		return super.stop()
	}

	private fun checkAcceleration() {
		val currentCoordinates = Client.getEntityCoords(playerPed) ?: return

		playerCoordinates?.let {
			val dateNow = Date.now() / 1_000
			val dt = dateNow - iLastSpeedCheck
			val iSpeed = Client.getEntitySpeed(playerPed)

			iLastSpeedCheck = dateNow

			iPlayerAcceleration = (iSpeed - iPlayerSpeed) / dt
			iPlayerSpeed = iSpeed

			iPlayerAccelerationModule = if (iPlayerAcceleration >= 0) iPlayerAcceleration else -iPlayerAcceleration

			if (iPlayerAccelerationModule >= accelerationThreshold) {
				if (!isAccelerationThresholdAcchieved) {
					isAccelerationThresholdAcchieved = true

					if (!Client.isPedAtGetInAnyVehicle(playerPed)) {
						UEvent.emit(
							AccelerationThresholdAchievedEvent(
								iPlayerAcceleration,
								iPlayerAccelerationModule
							)
						)
					}
				}
			} else {
				isAccelerationThresholdAcchieved = false
			}
		}

		if (playerCoordinates != currentCoordinates) {
			val rotation = Client.getEntityHeading(playerPed)
			val coordinates = CoordinatesX(currentCoordinates, rotation)

			playerCoordinates = coordinates

			UEvent.emit(
				PlayerCoordinatesChangedEvent(
					coordinates
				)
			)
		}
	}

	private fun checkFadeInOut(isFadeOut: Boolean) {
		if (isFadeOut != this.isFadeOut) {
			this.isFadeOut = isFadeOut
			UEvent.emit(ScreenFadeOutEvent(isFadeOut))
		}
	}

	private fun checkPlayersPed(ped: Int) {
		if (playerPed != ped) {
			playerPed = ped

			UEvent.emit(PlayersPedChangedEvent(ped))
		}
	}

	private fun checkVehicleHealth() {
		val currentPedHealth = Client.getEntityHealth(playerPed)
		val pedHealthDiff = currentPedHealth - playerPedHealth

		if (pedHealthDiff > 0) {
			if (currentPedHealth == 0) {
				UEvent.emit(PlayerPedUnconsciousEvent(pedHealthDiff))
			} else {
				UEvent.emit(PlayersPedHealthChangedEvent(currentPedHealth, pedHealthDiff))
			}
		}

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
			PlayersVehicleHealthChangedEvent(
				bodyHealth = bodyHealth,
				bodyDiff = bodyHealth - (vehicleBodyHealth ?: bodyHealth),

				engineHealth = engineHealth,
				engineDiff = engineHealth - (vehicleEngineHealth ?: engineHealth),

				petrolTankHealth = petrolTankHealth,
				petrolTankDiff = petrolTankHealth - (vehiclePetrolTankHealth ?: petrolTankHealth),

				pedHealth = currentPedHealth,
				pedDiff = pedHealthDiff
			)
		)

		vehicleBodyHealth = bodyHealth
		vehicleEngineHealth = engineHealth
		vehiclePetrolTankHealth = petrolTankHealth
	}

	private fun repeatJob(timeMillis: Long, function: () -> Unit): Job = launch {
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

	private fun checkRadio() {
		val currentRadio =
			if (playersVehicle?.let { Client.getIsVehicleEngineRunning(it) } == true) Client.getRadioStation() else null

		if (currentRadio != playerRadioStationName) {
			UEvent.emit(PlayerRadioStationChangedEvent(currentRadio))

			playerRadioStationName = currentRadio

			if (currentRadio != null) {
				UEvent.emit(PlayerVehicleRadioEnabledEvent(currentRadio))
			} else {
				UEvent.emit(PlayerVehicleRadioDisabledEvent())
			}
		}
	}

	private fun checkIsPlayerInVehicle() {
		val currentVehicle = if (Client.isPedInAnyVehicle(playerPed)) Client.getVehiclePedIsUsing(playerPed) else null

		if (currentVehicle == playersVehicle) return

		if (currentVehicle == null) {
			UEvent.emit(PlayerLeftVehicleEvent())
		} else {
			val seat = Client.getPassengerSeatOfPedInVehicle(currentVehicle, playerPed) ?: return

			UEvent.emit(
				PlayerJoinVehicleEvent(
					currentVehicle,
					seat
				)
			)
		}

		UEvent.emit(PlayersVehicleChangedEvent(currentVehicle))
		playersVehicle = currentVehicle
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