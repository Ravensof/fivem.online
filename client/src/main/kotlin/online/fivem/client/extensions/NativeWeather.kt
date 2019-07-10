@file:Suppress("DEPRECATION")

package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.gtav.NativeWeather

fun NativeWeather.setOverTime(time: Float) {
	Natives.setWeatherTypeOverTime(name, time)
}

fun NativeWeather.setWeatherTypePersist() {
	Natives.setWeatherTypePersist(name)
}

fun NativeWeather.setWeatherTypeNow() {
	Natives.setWeatherTypeNow(name)
}

fun NativeWeather.setWeatherTypeNowPersist() {
	Natives.setWeatherTypeNowPersist(name)
}

fun NativeWeather.mix(nativeWeather: NativeWeather, weather2: Float) {
	Natives.setWeatherTypeTransition(this, nativeWeather, weather2)
}

