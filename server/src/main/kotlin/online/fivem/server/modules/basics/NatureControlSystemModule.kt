package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Utils
import online.fivem.common.common.VDate
import online.fivem.common.entities.Weather
import online.fivem.common.extensions.repeatJob
import online.fivem.common.gtav.NativeWeather
import kotlin.coroutines.CoroutineContext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class NatureControlSystemModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val synchronizationModule by moduleLoader.onReady<SynchronizationModule>()

	override fun onStart(): Job? {
		repeatJob(CALCULATE_WEATHER_PERIOD_SECONDS * 1_000) {

			val currentTemperature = currentTemperature(synchronizationModule.date)
			val cal = calculations(synchronizationModule.date)
			val diff = cal.second - cal.first
			val weather: NativeWeather

			if (currentTemperature < 0) {
				weather = NativeWeather.XMAS
			} else {

				weather = when {

					diff < -6.0 && currentTemperature < 5 -> {
						NativeWeather.BLIZZARD //сильный ветер, осадки в виде снега
					}

					diff < -3.0 && currentTemperature < 5 -> {
						NativeWeather.SNOWLIGHT
					}

					diff < 0.5 -> {
						NativeWeather.EXTRASUNNY
					}

					diff < 1.0 -> {
						NativeWeather.CLEAR
					}

					diff < 1.5 -> {
						NativeWeather.CLOUDS
					}

					diff < 2.0 -> {
						NativeWeather.OVERCAST//небо затянуто облаками
					}

					diff < 2.5 -> {
						NativeWeather.FOGGY//облачно
					}

					diff < 4.0 -> {
						NativeWeather.RAIN
					}

//					diff < 0 -> {
//						NativeWeather.CLEARING// моросящий дождь, слабооблачно
//					}
//
//					diff < 0 -> {
//						NativeWeather.SMOG
//					}

					else -> {
						NativeWeather.THUNDER
					}
				}
			}

			Console.debug("weather calculated: ${weather.name} ${currentTemperature}tC (${cal.first}\\${cal.second} -> $diff)")

			synchronizationModule.syncData.weather = Weather(
				weather = weather,
				appearanceTime = synchronizationModule.date.time + synchronizationModule.date.timeSpeed * 60_000,
				temperature = currentTemperature
			)
		}

		return super.onStart()
	}

	private fun currentTemperature(microtime: Double): Double = currentTemperature(VDate(microtime))

	private fun currentTemperature(date: VDate): Double {
		var temp = 0.0

		TemperaturePeriods.values().forEach {
			temp += it.function(date)
		}

		return temp
	}

	private fun calculateAverageTemp(timeStart: Long, timeEnd: Long): Double {
		var temperature = 0.0
		val step: Long = (timeEnd - timeStart) / NUMBER_OF_CALCULATIONS_ON_RANGE

		for (i in timeStart..timeEnd step step) {
			temperature += currentTemperature(i.toDouble())
		}

		return temperature * step / (timeEnd - timeStart)
	}

	private fun calculations(date: VDate): Pair<Double, Double> {

		val period = 86400_000 * 7

		return calculateAverageTemp((date.time - period).toLong(), date.time.toLong()) to
				calculateAverageTemp(date.time.toLong(), (date.time + period).toLong())
	}

	private enum class TemperaturePeriods(val function: (VDate) -> Double) {
		YEAR({ cos(getX(it) / (PI * 18)) * 15 + 10 }),
		DAY({ sin((getX(it) - 0.2) * PI) * 3 }),

		HUNDRED_YEARS_NOISE({ sin(getX(it) / (PI * 1800)) * 10 }),
		YEARS_NOISE({ sin(getX(it) / 10) * 5 }),
	}

	companion object {

		private fun getX(date: VDate): Double {
			return date.time * Utils.MILLISECONDS_TO_DAY + DAY_LAG
		}

		private const val DAY_LAG = 180
		private const val CALCULATE_WEATHER_PERIOD_SECONDS: Long = 60

		private const val NUMBER_OF_CALCULATIONS_ON_RANGE = 1_000
	}
}