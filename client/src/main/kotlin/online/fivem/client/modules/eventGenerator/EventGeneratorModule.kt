package online.fivem.client.modules.eventGenerator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import online.fivem.client.entities.Ped
import online.fivem.client.entities.Vehicle
import online.fivem.client.events.*
import online.fivem.client.extensions.getSeatOfPedInVehicle
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.EntityId
import online.fivem.common.common.UEvent
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.extensions.onNull
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
			val playerPed = playerPed ?: return@repeatJob

			checkVehicleHealth(playerPed)
			checkAcceleration(playerPed)
		}

		repeatJob(500) {
			checkPauseMenuState(Client.getPauseMenuState())

			val playerPed = playerPed ?: return@repeatJob

			checkIsPlayerInVehicle(playerPed)
			checkPlayerTryingToGetAnyVehicle()
			checkRadio()
		}

		repeatJob(1_000) {
			checkAudioMusicLevelInMP(Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero())
			checkIsScreenFadedInOut(Client.isScreenFadedOut())

			val pedIndex = Client.getPlayerPedId().takeIf { it != 0 } ?: return@repeatJob
			val playerPed = checkPlayersPed(pedIndex) ?: return@repeatJob

			checkCoordinates(playerPed)
		}

		return super.onStart()
	}

	override fun onStop(): Job? {
		cancel()

		return super.onStop()
	}

	private fun checkPlayerTryingToGetAnyVehicle() {
		val isPedAtGetInAnyVehicleRightNow = playerPed?.isAtGetInAVehicle == true

		if (isPedAtGetInAnyVehicleRightNow != isPedAtGetInAnyVehicle) {
			playerPed?.getVehicleIsUsing()?.let { vehicle ->
				UEvent.emit(PlayerTryingToGetVehicle(vehicle))
			}.onNull {
				UEvent.emit(PlayerCancelsTryingToGetVehicle())
			}
			isPedAtGetInAnyVehicle = isPedAtGetInAnyVehicleRightNow
		}
	}

	private fun checkCoordinates(playerPed: Ped) {
		val currentCoordinates = playerPed.coordinates

		if (playerCoordinates != currentCoordinates) {
			val rotation = playerPed.heading
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

	private fun checkAcceleration(playerPed: Ped) {
		playerCoordinates?.let {
			val dateNow = Date.now() / 1_000
			val dt = dateNow - iLastSpeedCheck
			val iSpeed = playerPed.getSpeed()

			iLastSpeedCheck = dateNow

			playerAcceleration = (iSpeed - playerSpeed) / dt
			playerSpeed = iSpeed

			if (playerAcceleration.absoluteValue >= accelerationThreshold) {
				if (!playerPed.isAtGetInAVehicle) {
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

	private fun checkPlayersPed(ped: EntityId): Ped? {
		if (playerPed?.entity != ped) {
			val newPed = Ped.newInstance(ped)

			playerPed = newPed

			UEvent.emit(PlayersPedChangedEvent(newPed))

			return newPed
		}

		return null
	}

	private fun checkVehicleHealth(playerPed: Ped) {

		val currentPedHealth = playerPed.health
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

		val bodyHealth = vehicle.bodyHealth
		val engineHealth = vehicle.engineHealth
		val petrolTankHealth = vehicle.petrolTankHealth

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
		val previousSeat = playerSeatIndex

		if (seatIndex == previousSeat) return

		when {
			previousSeat != null && seatIndex != null -> UEvent.emit(
				if (seatIndex == -1)
					PlayerVehicleSeatEvent.Changed.AsDriver(seatIndex)
				else
					PlayerVehicleSeatEvent.Changed.AsPassenger(previousSeat, seatIndex)
			)

			previousSeat != null && seatIndex == null -> UEvent.emit(
				PlayerVehicleSeatEvent.Left(previousSeat)
			)

			previousSeat == null && seatIndex != null -> UEvent.emit(
				if (seatIndex == -1) {
					PlayerVehicleSeatEvent.Join.Driver()
				} else {
					PlayerVehicleSeatEvent.Join.Passenger(seatIndex)
				}
			)
		}

		playerSeatIndex = seatIndex
	}

	private fun checkRadio() {
		val currentRadio =
			if (playersVehicle?.isEngineRunning == true) Client.getRadioStation() else null

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

	private fun checkIsPlayerInVehicle(playerPed: Ped) {
		val currentVehicle = playerPed.getVehicleIsIn()?.let { Vehicle.newInstance(it) }
		val previousVehicle = playersVehicle

		if (currentVehicle == previousVehicle) return

		val playerSeatIndex = currentVehicle?.let { Client.getSeatOfPedInVehicle(it.entity, playerPed.entity) }
		checkPlayerSeatIndex(playerSeatIndex)

		when {
			previousVehicle != null && currentVehicle != null -> UEvent.emit(
				PlayerLeftOrJoinVehicleEvent.Changed(currentVehicle, previousVehicle)
			)

			previousVehicle != null && currentVehicle == null -> UEvent.emit(
				PlayerLeftOrJoinVehicleEvent.Left(previousVehicle)
			)

			previousVehicle == null && currentVehicle != null -> UEvent.emit(
				if (playerSeatIndex == -1)
					PlayerLeftOrJoinVehicleEvent.Join.Driver(currentVehicle)
				else
					PlayerLeftOrJoinVehicleEvent.Join.Passenger(currentVehicle)
			)
		}

		playersVehicle = currentVehicle
	}

	companion object {

		val playerId get() = Client.getPlayerId()

		var playerPed: Ped? = null
			private set
		var playerPedHealth: Int = 0
			private set
		var playersVehicle: Vehicle? = null
			private set

		var vehicleBodyHealth: Int? = null
			private set
		var vehicleEngineHealth: Double? = null
			private set
		var vehiclePetrolTankHealth: Double? = null
			private set
		var playerCoordinates: CoordinatesX? = null

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