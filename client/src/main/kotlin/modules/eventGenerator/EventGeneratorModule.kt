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
import online.fivem.common.events.*
import online.fivem.common.extensions.orZero
import online.fivem.common.extensions.repeatJob
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
//	private var isAccelerationThresholdAchieved = false

	override fun init() {
		moduleLoader.add(KeysHandlerModule(coroutineContext))
	}

	override fun start(): Job? {

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
			checkIsScreenFadedInOut(Client.isScreenFadedOut())
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
//				if (!isAccelerationThresholdAchieved) {
//					isAccelerationThresholdAchieved = true

				if (!Client.isPedAtGetInAnyVehicle(playerPed)) {
					UEvent.emit(
						AccelerationThresholdAchievedEvent(
							iPlayerAcceleration,
							iPlayerAccelerationModule,
							dt
						)
					)
				}
//				}
			} //else {
//				isAccelerationThresholdAchieved = false
//			}
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
		val currentPedHealth = Client.getEntityHealth(playerPed)
		val pedHealthDiff = currentPedHealth - playerPedHealth
		playerPedHealth = currentPedHealth

		if (pedHealthDiff != 0) {
			if (currentPedHealth == 0) {
				UEvent.emit(PlayerPedUnconsciousEvent(pedHealthDiff))
			} else {
				if (pedHealthDiff > 0) {
					UEvent.emit(PlayersPedHealthIncreasedEvent(currentPedHealth, pedHealthDiff))
				} else {
					UEvent.emit(PlayersPedHealthDropppedEvent(currentPedHealth, pedHealthDiff))
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
			if (pauseMenuState == 0) {
				UEvent.emit(PauseMenuDisabledEvent())
			} else {
				UEvent.emit(PauseMenuStateChangedEvent(state))
			}

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
}