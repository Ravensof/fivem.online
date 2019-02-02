package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.extensions.setOverTime
import online.fivem.client.extensions.setWeatherTypeNow
import online.fivem.client.extensions.setWeatherTypeNowPersist
import online.fivem.client.extensions.setWeatherTypePersist
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Stack
import online.fivem.common.gtav.NativeWeather
import kotlin.coroutines.CoroutineContext

class WeatherModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val api by moduleLoader.onReady<API>()

	private var blackOutHandle = Stack.UNDEFINED_INDEX

	override fun onStart(): Job? {

		setWeather(DEFAULT_WEATHER)

		return super.onStart()
	}

	fun setWeather(weather: NativeWeather) = launch {

		weather.setOverTime(WEATHER_CHANGING_SECONDS.toFloat())
		delay(WEATHER_CHANGING_SECONDS.toLong() * 1_000)

		delay(100)

		Client.clearOverrideWeather()
		Client.clearWeatherTypePersist()
		weather.setWeatherTypePersist()
		weather.setWeatherTypeNow()
		weather.setWeatherTypeNowPersist()

		if (weather == NativeWeather.XMAS) {
			Client.setForceVehicleTrails(true)
			Client.setForcePedFootstepsTracks(true)
		} else {
			Client.setForceVehicleTrails(false)
			Client.setForcePedFootstepsTracks(false)
		}
	}

	fun setBlackOut(enable: Boolean) {
		api.unSetBlackOut(blackOutHandle)

		if (enable) {
			blackOutHandle = api.setBlackOut()
		}
	}

	companion object {
		const val WEATHER_CHANGING_SECONDS = 15
		val DEFAULT_WEATHER = NativeWeather.CLOUDS
	}
}