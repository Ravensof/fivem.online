package online.fivem.common.common

import online.fivem.common.extensions.compareTo
import online.fivem.common.extensions.times

object Utils {

	fun mpsToKmh(metersPerSeconds: Number): Number {
		return metersPerSeconds * 3.6
	}

	fun mpsToMph(metersPerSeconds: Number): Number {
		return metersPerSeconds * 2.236936
	}

	fun <T : Number> normalizeToLimits(value: T, min: T, max: T): T {
		if (value < min) return min
		if (value > max) return max
		return value
	}
}