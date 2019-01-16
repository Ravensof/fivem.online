package online.fivem.common.common

import online.fivem.common.extensions.times

object Utils {

	fun mpsToKmh(metersPerSeconds: Number): Number {
		return metersPerSeconds * 3.6
	}

	fun mpsToMph(metersPerSeconds: Number): Number {
		return metersPerSeconds * 2.236936
	}
}