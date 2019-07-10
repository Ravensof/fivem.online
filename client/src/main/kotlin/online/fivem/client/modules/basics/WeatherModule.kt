package online.fivem.client.modules.basics

import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.Natives
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.extensions.setOverTime
import online.fivem.client.extensions.setWeatherTypeNow
import online.fivem.client.extensions.setWeatherTypeNowPersist
import online.fivem.client.extensions.setWeatherTypePersist
import online.fivem.common.common.Console
import online.fivem.common.entities.Weather
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.gtav.NativeWeather

class WeatherModule(
	private val dateTimeModule: DateTimeModule
) : AbstractClientModule() {

	private val weatherQueue = Channel<Weather>(10)

	private var currentWeather: NativeWeather = NativeWeather.OVERCAST
	private var currentTemperature = 0.0

	override fun onStartAsync() = async {
		dateTimeModule.waitForStart()

		setWeather(currentWeather, 1f)

		this@WeatherModule.launch {
			for (weather in weatherQueue) {
				currentTemperature = weather.temperature

				if (weather.weatherName == currentWeather.code) continue

				val newWeather = NativeWeather.valueOf(weather.weatherName)

				val changingTime = (weather.appearanceTime - dateTimeModule.date.time) / dateTimeModule.date.timeSpeed

				Console.debug("request changing weather from ${currentWeather.code} to ${weather.weatherName} in $changingTime ms")

				setWeather(newWeather, changingTime.toFloat()).join()
			}
		}
	}

	override fun onSync(serverData: ServerSideSynchronizationEvent) = launch {
		serverData.weather?.let {
			weatherQueue.send(it)
		}
	}

	private fun setWeather(
		weather: NativeWeather,
		changingMilliseconds: Float = WEATHER_CHANGING_MILLISECONDS.toFloat()
	) =
		launch {
			val changingTime = if (changingMilliseconds > 1) changingMilliseconds else 1f

			currentWeather = weather
			weather.setOverTime(changingTime / 1_000)
			delay(changingTime.toLong())

			Natives.clearOverrideWeather()
			Natives.clearWeatherTypePersist()
			weather.setWeatherTypePersist()
			weather.setWeatherTypeNow()
			weather.setWeatherTypeNowPersist()

			if (weather == NativeWeather.XMAS) {
				Natives.setForceVehicleTrails(true)
				Natives.setForcePedFootstepsTracks(true)
			} else {
				Natives.setForceVehicleTrails(false)
				Natives.setForcePedFootstepsTracks(false)
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