package online.fivem.client.modules.vehicle.test

import online.fivem.common.extensions.orZero

abstract class Transmission {

	var accelerate = 0.0 //0-1
	var brake = 0.0 //0-1

	abstract fun getRatio(): Double
}

class BaseTransmission : Transmission() {

	var clutch = 0.0

	var gear: String = "N"

	val gears = mapOf(
		"R" to -3.223,
		"N" to 0.0,
		"1" to 3.166,
		"2" to 1.882,
		"3" to 1.296,
		"4" to 0.972,
		"5" to 0.738
	)

	override fun getRatio(): Double {
		return gears[gear].orZero()
	}

	fun getGears(): List<Double> {
		return gears.map { it.value }
	}
}