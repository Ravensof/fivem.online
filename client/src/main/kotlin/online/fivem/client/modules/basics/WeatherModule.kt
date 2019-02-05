package online.fivem.client.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.client.extensions.setOverTime
import online.fivem.client.extensions.setWeatherTypeNow
import online.fivem.client.extensions.setWeatherTypeNowPersist
import online.fivem.client.extensions.setWeatherTypePersist
import online.fivem.client.gtav.Client
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.Weather
import online.fivem.common.gtav.NativeWeather
import kotlin.coroutines.CoroutineContext

class WeatherModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	val weatherQueue = Channel<Weather>(10)

	private val dateTimeModule by moduleLoader.onReady<DateTimeModule>()

	private var currentWeather: NativeWeather = GlobalConfig.defaultWeather
	private var currentTemperature = 0.0

	override fun onStart(): Job? {
		setWeather(currentWeather, 1f)

		launch {
			for (weather in weatherQueue) {
				currentTemperature = weather.temperature

				if (weather.weather == currentWeather) continue

				val changingTime = (weather.appearanceTime - dateTimeModule.date.time) / dateTimeModule.date.timeSpeed

				Console.debug("request changing weather from ${currentWeather.name} to ${weather.weather.name} in $changingTime ms")

				setWeather(weather.weather, if (changingTime > 1) changingTime.toFloat() else 1f).join()
			}
		}

		return super.onStart()
	}

	private fun setWeather(
		weather: NativeWeather,
		changingMilliseconds: Float = WEATHER_CHANGING_MILLISECONDS.toFloat()
	) =
		launch {

			currentWeather = weather
			weather.setOverTime(changingMilliseconds / 1_000)
			delay((changingMilliseconds).toLong())

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

//	private fun test() {
//
//		val list = listOf(
//			NativeWeather.EXTRASUNNY,
//			NativeWeather.CLEAR,
//
//			NativeWeather.CLOUDS,
//			NativeWeather.OVERCAST,//небо затянуто облаками
//			NativeWeather.FOGGY,//облачно
//			NativeWeather.RAIN,
//			NativeWeather.THUNDER,
//
//			NativeWeather.CLEARING,// моросящий дождь, слабооблачно
//
//			NativeWeather.SMOG,
//
//			NativeWeather.SNOWLIGHT,
//			NativeWeather.XMAS,
//
//			NativeWeather.BLIZZARD//сильный ветер, снег
//		)
//
//		launch {
//			while (true) {
//
//				list.forEach {
//					Console.debug(it.name)
//					setWeather(it, 15f).join()
//					delay(10_000)
//				}
//			}
//		}
//	}

	companion object {
		const val WEATHER_CHANGING_MILLISECONDS = 15_000
	}
}