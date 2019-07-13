package online.fivem.client.modules.basics

import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import online.fivem.Natives
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.common.CoordinatesEvent
import online.fivem.client.common.GlobalCache.player
import online.fivem.client.entities.Ped
import online.fivem.client.entities.Vehicle
import online.fivem.client.events.*
import online.fivem.client.extensions.getSeatOfPedInVehicle
import online.fivem.client.extensions.isAnyRadioTrackPlaying
import online.fivem.client.extensions.isControlPressed
import online.fivem.client.modules.basics.state_repository_modules.ProfileSettingsRepositoryModule
import online.fivem.common.common.Event
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.extensions.orZero
import online.fivem.common.extensions.receiveAndCancel
import online.fivem.common.extensions.repeatJob
import online.fivem.common.gtav.NativeControls
import online.fivem.common.gtav.RadioStation
import online.fivem.enums.PauseMenuState
import kotlin.js.Date
import kotlin.math.absoluteValue

class StateRepositoryModule(
	private val bufferedActionsModule: BufferedActionsModule
) : AbstractClientModule() {

	private val profileSettingsRepositoryModule = ProfileSettingsRepositoryModule()

	val profileSettings = profileSettingsRepositoryModule.profileSettingsProvider

	val playerPed = ConflatedBroadcastChannel<Ped>()

	val isPlayerTalking = ConflatedBroadcastChannel<Boolean>()

	val coordinatesX = ConflatedBroadcastChannel<CoordinatesX>()

	val radioStation = ConflatedBroadcastChannel<RadioStation?>()

	val pauseMenuState = ConflatedBroadcastChannel<PauseMenuState>()

	val isPauseMenuEnabled = ConflatedBroadcastChannel<Boolean>()

	val playerSpeed = ConflatedBroadcastChannel<Double>()

	val activePlayers = ConflatedBroadcastChannel<Array<Int>>()

	val talkingPlayers = ConflatedBroadcastChannel<List<Int>>()

	var accelerationThreshold: Double = 150.0 // m/s^2
		set(value) {
			if (value > field) {
				field = value
			}
		}


	private var iLastSpeedCheck = 0.0

	private var vehiclePlayerTryingToGet: Vehicle? = null

	private var isRadioEnabled = false


	override suspend fun onInit() {
		super.onInit()

		moduleLoader.add(profileSettingsRepositoryModule)
	}

	override fun onStartAsync() = async {
		bufferedActionsModule.waitForStart()
		profileSettingsRepositoryModule.waitForStart()

		this@StateRepositoryModule.repeatJob(25) {
			checkIsPlayerTalking(player.id)
			checkTalkingPlayers()
		}

		this@StateRepositoryModule.repeatJob(50) {
			checkVehicleHealth(player.ped)
			checkAcceleration(player.ped)
		}

		this@StateRepositoryModule.repeatJob(500) {
			checkPauseMenuState(Natives.getPauseMenuState())

			checkIsPlayerInVehicle(player.ped)
			checkPlayerTryingToGetAnyVehicle(player.ped)
			checkRadio()
		}

		this@StateRepositoryModule.repeatJob(1_000) {

			checkActivePlayers()
			checkPlayersPed(player.ped)

			checkCoordinatesX(player.ped)

			profileSettingsRepositoryModule.check().join()
		}
	}

	private suspend fun checkIsPlayerTalking(playerId: Int) {
		val value = Natives.networkIsPlayerTalking(playerId) || NativeControls.Keys.PUSH_TO_TALK.isControlPressed()

		if (isPlayerTalking.valueOrNull == value) return

		isPlayerTalking.send(value)
	}

	private suspend fun checkActivePlayers() {
		val currentActivePlayers = Natives.getActivePlayers()

		if (activePlayers.valueOrNull?.contentEquals(currentActivePlayers) == true) return

		activePlayers.send(currentActivePlayers)
	}

	private suspend fun checkTalkingPlayers() {
		val currentTalkingPlayers = activePlayers.openSubscription().receiveAndCancel()
			.filter { Natives.networkIsPlayerTalking(it) }

		if (talkingPlayers.valueOrNull != currentTalkingPlayers) {
			talkingPlayers.send(currentTalkingPlayers)
		}
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

		if (coordinatesX.valueOrNull != currentCoordinatesX) {

			if (bufferedActionsModule.coordinatesLocker.isLocked()) return

			Event.emit(
				PlayerCoordinatesChangedEvent(
					currentCoordinatesX
				)
			)
			CoordinatesEvent.handle(currentCoordinatesX)
			coordinatesX.send(currentCoordinatesX)
		}
	}

	private suspend fun checkAcceleration(playerPed: Ped) {
		val dateNow = Date.now() / 1_000
		val dt = dateNow - iLastSpeedCheck
		val iSpeed = playerPed.getSpeed()

		iLastSpeedCheck = dateNow

		playerAcceleration = (iSpeed - playerSpeed.valueOrNull.orZero()) / dt

		if (iSpeed != playerSpeed.valueOrNull.orZero()) {
			playerSpeed.send(iSpeed)
		}

		if (playerAcceleration.absoluteValue >= accelerationThreshold) {
			if (!playerPed.isTryingToGetInAnyVehicle()) {

				if (bufferedActionsModule.coordinatesLocker.isLocked()) return

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

	private suspend fun checkPlayersPed(ped: Ped) {
		if (playerPed.valueOrNull?.entityId != ped.entityId) {

			playerPed.send(ped)
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

	private suspend fun checkPauseMenuState(state: PauseMenuState) {
		if (pauseMenuState.valueOrNull != state) {

			val isPauseMenuEnabled = state != PauseMenuState.DISABLED

			if (this.isPauseMenuEnabled.valueOrNull != isPauseMenuEnabled) {
				this.isPauseMenuEnabled.send(isPauseMenuEnabled)
			}

			pauseMenuState.send(state)
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
			if (playersVehicle?.isEngineRunning() == true && (isRadioEnabled || Natives.isAnyRadioTrackPlaying())) Natives.getRadioStation() else null

		if (currentRadio != radioStation.valueOrNull) {

			isRadioEnabled = currentRadio != null

			radioStation.send(currentRadio)
		}
	}

	private suspend fun checkIsPlayerInVehicle(playerPed: Ped) {
		val currentVehicle = playerPed.getVehicleIsIn()
		val previousVehicle = playersVehicle

		if (currentVehicle == previousVehicle) return

		val playerSeatIndex = currentVehicle?.let { Natives.getSeatOfPedInVehicle(it.entityId, playerPed.entityId) }
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

		private var playerPedHealth: Int = 0

		private var playersVehicle: Vehicle? = null

		private var vehicleBodyHealth: Int? = null

		private var vehicleEngineHealth: Double? = null

		private var vehiclePetrolTankHealth: Double? = null

		private var playerSeatIndex: Int? = null

		private var playerAcceleration = 0.0
	}
}