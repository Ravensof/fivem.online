package online.fivem.client.modules.eventGenerator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import online.fivem.client.extensions.getPassengerSeatOfPedInVehicle
import online.fivem.client.extensions.isPedAtGetInAnyVehicle
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.UEvent
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.local.*
import online.fivem.common.extensions.orZero
import online.fivem.common.extensions.repeatJob
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date
import kotlin.math.absoluteValue

class EventGeneratorModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	private var isFadeOut: Boolean = true

	var accelerationThreshold: Double = 150.0 // m/s^2
		set(value) {
			if (value > field) {
				field = value
			}
		}
	private var iLastSpeedCheck = 0.0

	private var isPedAtGetInAnyVehicle: Boolean? = null

	override fun onInit() {
	}

	override fun onStart(): Job? {

		repeatJob(50) {
			if (playerPed == 0) return@repeatJob
			checkVehicleHealth()
			checkAcceleration()
		}

		repeatJob(500) {
			if (playerPed == 0) return@repeatJob
			checkPlayerSeatIndex(Client.getPassengerSeatOfPedInVehicle())
			checkPauseMenuState(Client.getPauseMenuState())
			checkIsPlayerInVehicle()
			checkPlayerTryingToGetAnyVehicle()
			checkRadio()
		}

		repeatJob(1_000) {
			checkPlayersPed(Client.getPlayerPed())
			if (playerPed == 0) return@repeatJob

			checkCoordinates()
			checkAudioMusicLevelInMP(Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero())
			checkIsScreenFadedInOut(Client.isScreenFadedOut())
		}

		return super.onStart()
	}

	override fun onStop(): Job? {
		cancel()

		return super.onStop()
	}

	private fun checkPlayerTryingToGetAnyVehicle() {
		val isPedAtGetInAnyVehicleRightNow = Client.isPedAtGetInAnyVehicle(playerPed)

		if (isPedAtGetInAnyVehicleRightNow != isPedAtGetInAnyVehicle) {
			if (isPedAtGetInAnyVehicleRightNow) {
				val vehicle = Client.getVehiclePedIsUsing(playerPed) ?: return
				UEvent.emit(PlayerTryingToGetVehicle(vehicle))
			} else {
				UEvent.emit(PlayerCancelsTryingToGetVehicle())
			}
			isPedAtGetInAnyVehicle = isPedAtGetInAnyVehicleRightNow
		}
	}

	private fun checkCoordinates() {
		val currentCoordinates = Client.getEntityCoords(playerPed) ?: return

		if (playerCoordinates != currentCoordinates) {
			val rotation = Client.getEntityHeading(playerPed)
			val coordinates = CoordinatesX(currentCoordinates, rotation)

			playerCoordinates = coordinates

			UEvent.emit(
				PlayerCoordinatesChangedEvent(
					coordinates
				)
			)
			CoordinatesEvent.handle(currentCoordinates)
		}
	}

	private fun checkAcceleration() {
		playerCoordinates?.let {
			val dateNow = Date.now() / 1_000
			val dt = dateNow - iLastSpeedCheck
			val iSpeed = Client.getEntitySpeed(playerPed)

			iLastSpeedCheck = dateNow

			playerAcceleration = (iSpeed - playerSpeed) / dt
			playerSpeed = iSpeed

			if (playerAcceleration.absoluteValue >= accelerationThreshold) {
				if (!Client.isPedAtGetInAnyVehicle(playerPed)) {
					UEvent.emit(
						AccelerationThresholdAchievedEvent(
							playerAcceleration,
							playerAcceleration.absoluteValue,
							dt
						)
					)
				}
			}
		}
	}

	private fun checkIsScreenFadedInOut(isFadeOut: Boolean) {
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
		if (playerPed == 0) return

		val currentPedHealth = Client.getEntityHealth(playerPed)
		val pedHealthDiff = currentPedHealth - playerPedHealth
		playerPedHealth = currentPedHealth

		if (pedHealthDiff != 0) {
			if (currentPedHealth == 0) {
				UEvent.emit(PlayersPedHealthChangedEvent.Zero(currentPedHealth))
			} else {
				if (pedHealthDiff > 0) {
					UEvent.emit(PlayersPedHealthChangedEvent.Increased(currentPedHealth, pedHealthDiff))
				} else {
					UEvent.emit(PlayersPedHealthChangedEvent.Dropped(currentPedHealth, pedHealthDiff))
				}
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

	private fun checkPauseMenuState(state: Int) {
		if (pauseMenuState != state) {
			if (state == 0) {
				UEvent.emit(PauseMenuStateChangedEvent.Disabled())
			} else {
				UEvent.emit(PauseMenuStateChangedEvent.Switched(state))
			}

			pauseMenuState = state
		}
	}

	private fun checkAudioMusicLevelInMP(volume: Int) {
		if (audioMusicLevelInMP != volume) {
			UEvent.emit(ProfileSettingUpdatedEvent.AudioMusicLevelInMP(volume))

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
				UEvent.emit(PlayerVehicleRadioToggledEvent.Enabled(currentRadio))
			} else {
				UEvent.emit(PlayerVehicleRadioToggledEvent.Disabled())
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

		val playerId get() = Client.getPlayerId()

		var playerPed = 0
			private set
		var playerPedHealth: Int = 0
			private set
		var playersVehicle: Int? = null
			private set

		var vehicleBodyHealth: Int? = null
			private set
		var vehicleEngineHealth: Double? = null
			private set
		var vehiclePetrolTankHealth: Double? = null
			private set
		var playerCoordinates: CoordinatesX? = null
			private set
		var playerSeatIndex: Int? = null
			private set
		var pauseMenuState: Int? = null
			private set
		var audioMusicLevelInMP: Int? = null
			private set
		var playerRadioStationName: RadioStation? = null
			private set
		var playerSpeed = 0.0
			private set
		var playerAcceleration = 0.0
			private set
	}
}