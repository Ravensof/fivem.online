package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Natives
import online.fivem.common.GlobalConfig
import online.fivem.common.common.EntityId
import online.fivem.common.entities.Coordinates
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.RGB
import online.fivem.common.gtav.NativeWeather

suspend fun Client.createVehicle(
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

fun Client.networkResurrectLocalPlayer(coordinatesX: CoordinatesX, changeTime: Boolean = true) =
	Client.networkResurrectLocalPlayer(
		coordinatesX.x,
		coordinatesX.y,
		coordinatesX.z,
		coordinatesX.rotation,
		changeTime
	)

//ped садится в машину
fun Client.isPedAtGetInAnyVehicle(ped: EntityId): Boolean {
	return Client.isPedInAnyVehicle(ped) != (Client.getVehiclePedIsUsing(ped) != null)
}

//ped пытается сесть в машину
fun Client.isPedGettingInAnyVehicle(ped: EntityId): Boolean {
	return Client.isPedInAnyVehicle(ped, true) != Client.isPedInAnyVehicle(ped, false)
}

fun Client.drawScreenText2D(
	x: Double,
	y: Double,
	message: String,
	scale: Double = 1.0,
	dropShadow: Boolean = true,
	outline: Boolean = false
) {
	setTextScale(0.0, 0.55 * scale)
	setTextFont(0)
	setTextProportional(true)
	setTextScale(0.0, 0.3)
	setTextColour(180, 20, 20, 255)
	setTextDropShadow(0, 0, 0, 0, 255)
	setTextEdge(1, 0, 0, 0, 255)

	if (dropShadow) {
		setTextDropShadow()
	}

	if (outline) {
		setTextOutline()
	}

	setTextEntry("STRING")
	addTextComponentString(message)
	drawText(x, y)
}

fun Client.drawScreenText3D(
	coordinates: Coordinates,
	message: String,
	scale: Double = 1.0,
	dropShadow: Boolean = true,
	outline: Boolean = false
) {
	val screenCoordinates = world3dToScreen2d(coordinates.x, coordinates.y, coordinates.z)

	screenCoordinates?.let {
		drawScreenText2D(it.first.toDouble(), it.second.toDouble(), message, scale, dropShadow, outline)
	}
}

fun Client.getAverageFPS(): Double {
	return getFrameCount().toDouble() / getGameTimer()
}

fun Client.getCurrentFPS(): Double {
	return 1f / getFrameTime()
}

fun Client.setVehicleNeonLightsColour(vehicle: EntityId, color: RGB) {
	setVehicleNeonLightsColour(vehicle, color.red, color.green, color.blue)
}

fun Client.setVehicleTyreSmokeColor(vehicle: EntityId, color: RGB) {
	setVehicleTyreSmokeColor(vehicle, color.red, color.green, color.blue)
}

/**
 * -1 водитель
 * 0 справа от водителя
 * 1 позади водителя
 * 2 ...
 */
fun Client.getSeatOfPedInVehicle(vehicle: EntityId, ped: EntityId): Int? {
	for (i in -1 until Client.getVehicleMaxNumberOfPassengers(vehicle)) {
		if (getPedInVehicleSeat(vehicle, i) == ped) {
			return i
		}
	}

	return null
}

fun Client.requestCollisionAtCoordinates(coordinates: Coordinates) =
	requestCollisionAtCoordinates(coordinates.x, coordinates.y, coordinates.z)

fun Client.getHexHashKey(functionName: String): String {
	return "0x" + (getHashKey(functionName) and 0xFFFFFFFF).toString(16)
}

fun Client.invokeNative(functionName: String, vararg args: Any): Any {
	return Natives.invokeNative(getHexHashKey(functionName), *args)
}

fun Client.setWeatherTypeTransition(weatherType1: NativeWeather, weatherType2: NativeWeather, percentWeather2: Float) {
	@Suppress("DEPRECATION")
	setWeatherTypeTransition(
		getHexHashKey(weatherType1.name),
		getHexHashKey(weatherType2.name),
		percentWeather2
	)//todo test
}

fun Client.getPlayersOnline(): List<Int> {
	val list = mutableListOf<Int>()

	for (i in 0 until GlobalConfig.MAX_PLAYERS) {
		if (networkIsPlayerActive(i)) {
			list.add(i)
		}
	}
	return list
}

fun Client.cellFrontCamActivate(activate: Boolean) {
	Natives.invokeNative<Nothing>("0x2491A93618B7D838", activate)
}

fun Client.takePhoto() {
	Natives.invokeNative<Nothing>("0xa67c35c56eb1bd9d")
}

fun Client.clearPhoto() {
	Natives.invokeNative<Nothing>("0xd801cc02177fa3f1")
}

fun Client.wasPhotoTaken(): Boolean {
	return Natives.invokeNative<Number>("0x0d6ca79eeebd8ca3") == 1
}

fun Client.savePhoto(unk: Int) {
	Natives.invokeNative<Nothing>("0x3dec726c25a11bac")
}