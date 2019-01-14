package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.common.Entity
import online.fivem.common.entities.CoordinatesX

fun Client.createVehicle(
	modelHash: Int,
	coordinatesX: CoordinatesX,
	isNetwork: Boolean = true,
	thisScriptCheck: Boolean = false
) = Client.createVehicle(
	modelHash,
	coordinatesX.x,
	coordinatesX.y,
	coordinatesX.z,
	coordinatesX.rotation,
	isNetwork,
	thisScriptCheck
)


fun Client.getPassengerSeatOfPedInVehicle(): Int? {
	val ped = Client.getPlayerPed()
	val vehicle = Client.getVehiclePedIsUsing(ped) ?: return null

	return Client.getPassengerSeatOfPedInVehicle(vehicle, ped)
}

fun Client.networkResurrectLocalPlayer(coordinatesX: CoordinatesX, changeTime: Boolean = true) =
	Client.networkResurrectLocalPlayer(
		coordinatesX.x,
		coordinatesX.y,
		coordinatesX.z,
		coordinatesX.rotation,
		changeTime
	)

fun Client.getEntitySpeedKmH(entity: Int): Double = getEntitySpeed(entity) * 3.6

fun Client.getEntitySpeedMpH(entity: Int): Double = getEntitySpeed(entity) * 2.236936

fun Client.isPedAtGetInAnyVehicle(ped: Entity): Boolean {
	return Client.isPedInAnyVehicle(ped) != (Client.getVehiclePedIsUsing(ped) != null)
}

fun Client.isPedGettingInAnyVehicle(ped: Entity): Boolean {
	return Client.isPedInAnyVehicle(ped, true) != Client.isPedInAnyVehicle(ped, false)
}