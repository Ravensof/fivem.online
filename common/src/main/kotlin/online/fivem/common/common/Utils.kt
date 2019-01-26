package online.fivem.common.common

import online.fivem.common.extensions.compareTo

object Utils {

	const val MPS_TO_KILOMETERS_PER_HOUR = 3.6
	const val MPS_TO_MILES_PER_HOUR = 2.236936

	fun <T : Number> normalizeToLimits(value: T, min: T, max: T): T {
		if (value < min) return min
		if (value > max) return max
		return value
	}
}