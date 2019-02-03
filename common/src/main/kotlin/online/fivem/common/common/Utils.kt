package online.fivem.common.common

import online.fivem.common.extensions.compareTo

object Utils {

	const val MPS_TO_KILOMETERS_PER_HOUR = 3.6
	const val MPS_TO_MILES_PER_HOUR = 2.236936

	const val MICROSECONDS_TO_DAY: Double = 1.0 / 86_400_000_000.0
	const val MILLISECONDS_TO_DAY: Double = 1.0 / 86_400_000.0
	const val DAY_TO_MICROSECONDS: Double = 86_400_000_000.0

	fun <T : Number> normalizeToLimits(value: T, min: T, max: T): T {
		if (value < min) return min
		if (value > max) return max
		return value
	}
}