package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.common.Entity
import online.fivem.common.common.Utils
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.gtav.NativeControls
import online.fivem.common.gtav.NativeVehicleMods

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

fun Client.getEntitySpeedKmH(entity: Int): Double = Utils.mpsToKmh(getEntitySpeed(entity)).toDouble()

fun Client.getEntitySpeedMpH(entity: Int): Double = Utils.mpsToMph(getEntitySpeed(entity)).toDouble()

//ped садится в машину
fun Client.isPedAtGetInAnyVehicle(ped: Entity): Boolean {
	return Client.isPedInAnyVehicle(ped) != (Client.getVehiclePedIsUsing(ped) != null)
}

//ped пытается сесть в машину
fun Client.isPedGettingInAnyVehicle(ped: Entity): Boolean {
	return Client.isPedInAnyVehicle(ped, true) != Client.isPedInAnyVehicle(ped, false)
}

fun Client.disableControlAction(
	inputGroup: NativeControls.Groups = Client.defaultControlGroup,
	control: NativeControls.Keys,
	disable: Boolean = true
) {
	disableControlAction(inputGroup.index, control.index, disable)
}

fun Client.isControlPressed(
	inputGroup: NativeControls.Groups = Client.defaultControlGroup,
	control: NativeControls.Keys
): Boolean {
	return Client.isControlPressed(inputGroup.index, control.index)
}

fun Client.isDisabledControlJustPressed(
	inputGroup: NativeControls.Groups = Client.defaultControlGroup,
	control: NativeControls.Keys
): Boolean {
	return isDisabledControlJustPressed(inputGroup.index, control.index)
}

fun Client.isDisabledControlJustReleased(
	inputGroup: NativeControls.Groups = Client.defaultControlGroup,
	control: NativeControls.Keys
): Boolean {
	return isDisabledControlJustReleased(inputGroup.index, control.index)
}

fun Client.isDisabledControlPressed(
	inputGroup: NativeControls.Groups = Client.defaultControlGroup,
	control: NativeControls.Keys
): Boolean {
	return isDisabledControlPressed(inputGroup.index, control.index)
}

fun Client.drawScreenText2D(
	x: Double,
	y: Double,
	message: String,
	dropShadow: Boolean = true,
	outline: Boolean = false
) {
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

fun Client.isToggleModOn(vehicle: Entity, modType: NativeVehicleMods): Boolean {
	return isToggleModOn(vehicle, modType.id)
}

fun Client.toggleVehicleMod(vehicle: Entity, modType: NativeVehicleMods, toggle: Boolean) {
	toggleVehicleMod(vehicle, modType.id, toggle)
}