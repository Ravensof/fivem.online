package online.fivem.extensions

import online.fivem.Citizen
import online.fivem.Natives

fun Natives.onNet(eventName: String, action: Function<*>) {
	online.fivem.onNet(eventName, action)
}

fun Natives.invokeNative(functionName: String, vararg args: Any): Any {
	return Citizen.invokeNative(getHexHashKey(functionName), *args)
}

fun Natives.getHexHashKey(functionName: String): String {
	return "0x" + (getHashKey(functionName) and 0xFFFFFFFF).toString(16)
}

fun Natives.on(eventName: String, callback: Function<*>) {
	online.fivem.on(eventName, callback)
}

fun Natives.setTick(action: Function<*>) {
	online.fivem.setTick(action)
}