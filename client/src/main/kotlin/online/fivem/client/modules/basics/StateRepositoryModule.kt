package online.fivem.client.modules.basics

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.CoordinatesEvent
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.entities.Vehicle
import online.fivem.client.events.*
import online.fivem.client.extensions.getSeatOfPedInVehicle
import online.fivem.client.extensions.isAnyRadioTrackPlaying
import online.fivem.client.extensions.isControlPressed
import online.fivem.client.gtav.Client
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.extensions.orZero
import online.fivem.common.extensions.repeatJob
import online.fivem.common.gtav.NativeControls
import online.fivem.common.gtav.ProfileSetting
import online.fivem.common.gtav.RadioStation
import kotlin.js.Date
import kotlin.math.absoluteValue

class StateRepositoryModule(
	private val bufferedActionsModule: BufferedActionsModule
) : AbstractClientModule() {

	val isPlayerTalking = ConflatedBroadcastChannel<Boolean>()

	var accelerationThreshold: Double = 150.0 // m/s^2
		set(value) {
			if (value > field) {
				field = value
			}
		}
	private var iLastSpeedCheck = 0.0

	private var vehiclePlayerTryingToGet: Vehicle? = null

	private var isRadioEnabled = false

	override fun onStart() = launch {
		bufferedActionsModule.waitForStart()

		this@StateRepositoryModule.repeatJob(25) {
			checkIsPlayerTalking(player.id)
		}

		this@StateRepositoryModule.repeatJob(50) {
			checkVehicleHealth(player.ped)
			checkAcceleration(player.ped)
		}

		this@StateRepositoryModule.repeatJob(500) {
			checkPauseMenuState(Client.getPauseMenuState())

			checkIsPlayerInVehicle(player.ped)
			checkPlayerTryingToGetAnyVehicle(player.ped)
			checkRadio()
		}

		this@StateRepositoryModule.repeatJob(1_000) {
			checkAudioMusicLevelInMP(Client.getProfileSetting(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP).orZero())

			checkPlayersPed(player.ped)

			checkCoordinatesX(player.ped)
		}
	}

	private suspend fun checkIsPlayerTalking(playerId: Int) {
		val value = Client.networkIsPlayerTalking(playerId) || NativeControls.Keys.PUSH_TO_TALK.isControlPressed()

		if (isPlayerTalking.valueOrNull == value) return

		isPlayerTalking.send(value)
	}

	private suspend fun checkPlayerTryingToGetAnyVehicle(playerPed: Ped) {
		val vehiclePlayerTryingToGetNow =
			if (playerPed.isTryingToGetInAnyVehicle()) playerPed.getVehicleIsInteracted() else null

		if (vehiclePlayerTryingToGetNow != vehiclePlayerTryingToGet) {

			vehiclePlayerTryingToGet?.let {
				Event.emit(PlayerTryingToGetVehicleEvent.End(it))
			}

			vehiclePlayerTryingToGetNow?.let {
				Event.emit(PlayerTryingToGetVehicleEvent.Start(it))
			}

			vehiclePlayerTryingToGet = vehiclePlayerTryingToGetNow
		}
	}

	private suspend fun checkCoordinatesX(playerPed: Ped) {
		val currentCoordinatesX = playerPed.coordinatesX

		if (playerCoordinates != currentCoordinatesX) {

			playerCoordinates = currentCoordinatesX

			if (bufferedActionsModule.coordinatesLocker.isLocked()) return

			Event.emit(
				PlayerCoordinatesChangedEvent(
					currentCoordinatesX
				)
			)
			CoordinatesEvent.handle(currentCoordinatesX)
		}
	}

	private suspend fun checkAcceleration(playerPed: Ped) {
		playerCoordinates?.let {
			val dateNow = Date.now() / 1_000
			val dt = dateNow - iLastSpeedCheck
			val iSpeed = playerPed.getSpeed()

			iLastSpeedCheck = dateNow

			playerAcceleration = (iSpeed - playerSpeed) / dt
			playerSpeed = iSpeed

			if (playerAcceleration.absoluteValue >= accelerationThreshold) {
				if (!playerPed.isTryingToGetInAnyVehicle()) {

					if (bufferedActionsModule.coordinatesLocker.isLocked()) return@let

					Event.emit(
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

	private suspend fun checkPlayersPed(ped: Ped) {
		if (playerPed?.entity != ped.entity) {

			playerPed = ped

			Event.emit(PlayersPedChangedEvent(ped))
		}
	}

	private suspend fun checkVehicleHealth(playerPed: Ped) {

		val currentPedHealth = playerPed.health
		val pedHealthDiff = currentPedHealth - playerPedHealth
		playerPedHealth = currentPedHealth

		if (pedHealthDiff != 0) {
			if (currentPedHealth == 0) {
				Event.emit(PlayersPedHealthChangedEvent.Zero(pedHealthDiff))
			} else {
				if (pedHealthDiff > 0) {
					Event.emit(PlayersPedHealthChangedEvent.Increased(currentPedHealth, pedHealthDiff))
				} else {
					Event.emit(PlayersPedHealthChangedEvent.Dropped(currentPedHealth, pedHealthDiff))
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

		Event.emit(
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

	private suspend fun checkPauseMenuState(state: Int) {
		if (pauseMenuState != state) {
			when {
				state == 0 -> Event.emit(
					PauseMenuStateChangedEvent.Disabled(pauseMenuState)
				)

				pauseMenuState == 0 -> Event.emit(
					PauseMenuStateChangedEvent.Enabled(state)
				)

				else -> Event.emit(
					PauseMenuStateChangedEvent.Switched(state, pauseMenuState)
				)
			}

			pauseMenuState = state
		}
	}

	private suspend fun checkAudioMusicLevelInMP(volume: Int) {
		if (audioMusicLevelInMP != volume) {
			Event.emit(ProfileSettingUpdatedEvent.AudioMusicLevelInMP(volume))

			audioMusicLevelInMP = volume
		}
	}

	private suspend fun checkPlayerSeatIndex(seatIndex: Int?) {
		val previousSeat = playerSeatIndex

		if (seatIndex == previousSeat) return

		when {
			previousSeat != null && seatIndex != null -> Event.emit(
				if (seatIndex == -1)
					PlayerVehicleSeatEvent.Changed.AsDriver(seatIndex)
				else
					PlayerVehicleSeatEvent.Changed.AsPassenger(previousSeat, seatIndex)
			)

			previousSeat != null && seatIndex == null -> Event.emit(
				PlayerVehicleSeatEvent.Left(previousSeat)
			)

			previousSeat == null && seatIndex != null -> Event.emit(
				if (seatIndex == -1) {
					PlayerVehicleSeatEvent.Join.Driver()
				} else {
					PlayerVehicleSeatEvent.Join.Passenger(seatIndex)
				}
			)
		}

		playerSeatIndex = seatIndex
	}

	private suspend fun checkRadio() {
		val currentRadio =
			if (playersVehicle?.isEngineRunning() == true && (isRadioEnabled || Client.isAnyRadioTrackPlaying())) Client.getRadioStation() else null

		if (currentRadio != playerRadioStationName) {
			Event.emit(PlayerRadioStationChangedEvent(currentRadio))

			playerRadioStationName = currentRadio

			isRadioEnabled = currentRadio != null

			if (currentRadio != null) {
				Event.emit(PlayerVehicleRadioToggledEvent.Enabled(currentRadio))
			} else {
				Event.emit(PlayerVehicleRadioToggledEvent.Disabled())
			}
		}
	}

	private suspend fun checkIsPlayerInVehicle(playerPed: Ped) {
		val currentVehicle = playerPed.getVehicleIsIn()
		val previousVehicle = playersVehicle

		if (currentVehicle == previousVehicle) return

		val playerSeatIndex = currentVehicle?.let { Client.getSeatOfPedInVehicle(it.entity, playerPed.entity) }
		checkPlayerSeatIndex(playerSeatIndex)

		when {
			previousVehicle != null && currentVehicle != null -> Event.emit(
				PlayerLeftOrJoinVehicleEvent.Changed(currentVehicle, previousVehicle)
			)

			previousVehicle != null && currentVehicle == null -> Event.emit(
				PlayerLeftOrJoinVehicleEvent.Left(previousVehicle)
			)

			previousVehicle == null && currentVehicle != null -> Event.emit(
				if (playerSeatIndex == -1)
					PlayerLeftOrJoinVehicleEvent.Join.Driver(currentVehicle)
				else
					PlayerLeftOrJoinVehicleEvent.Join.Passenger(currentVehicle)
			)
		}

		playersVehicle = currentVehicle
	}

	companion object {

		private var playerPed: Ped? = null

		private var playerPedHealth: Int = 0

		private var playersVehicle: Vehicle? = null

		private var vehicleBodyHealth: Int? = null

		private var vehicleEngineHealth: Double? = null

		private var vehiclePetrolTankHealth: Double? = null

		private var playerCoordinates: CoordinatesX? = null

		private var playerSeatIndex: Int? = null

		private var pauseMenuState: Int = 0

		private var audioMusicLevelInMP: Int? = null

		private var playerRadioStationName: RadioStation? = null

		private var playerSpeed = 0.0

		private var playerAcceleration = 0.0
	}
}