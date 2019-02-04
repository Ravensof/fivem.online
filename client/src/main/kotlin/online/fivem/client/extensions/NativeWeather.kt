@file:Suppress("DEPRECATION")

package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeWeather

fun NativeWeather.setOverTime(time: Float) {
	Client.setWeatherTypeOverTime(name, time)
}

fun NativeWeather.setWeatherTypePersist() {
	Client.setWeatherTypePersist(name)
}

fun NativeWeather.setWeatherTypeNow() {
	Client.setWeatherTypeNow(name)
}

fun NativeWeather.setWeatherTypeNowPersist() {
	Client.setWeatherTypeNowPersist(name)
}

fun NativeWeather.mix(nativeWeather: NativeWeather, weather2: Float) {
	Client.setWeatherTypeTransition(this, nativeWeather, weather2)
}

