package online.fivem.common.extensions

fun Number?.orZero(): Number {
	return this ?: 0
}

operator fun Number.minus(number: Number): Number {
	return when (this) {
		is Float ->
			this - number.toFloat()

		is Double ->
			this - number.toDouble()

		is Long ->
			this - number.toLong()

		is Int ->
			this - number.toInt()

		is Short ->
			this - number.toShort()

		else -> (this.toDouble() - number.toDouble())
	}
}

operator fun Number.plus(number: Number): Number {
	return when (this) {
		is Float ->
			this + number.toFloat()

		is Double ->
			this + number.toDouble()

		is Long ->
			this + number.toLong()

		is Int ->
			this + number.toInt()

		is Short ->
			this + number.toShort()

		else -> (this.toDouble() + number.toDouble())
	}
}

operator fun Number.times(number: Number): Number {
	return when (this) {
		is Float ->
			this * number.toFloat()

		is Double ->
			this * number.toDouble()

		is Long ->
			this * number.toLong()

		is Int ->
			this * number.toInt()

		is Short ->
			this * number.toShort()

		else -> (this.toDouble() * number.toDouble())
	}
}

operator fun Number.compareTo(number: Number): Int {
	return when (this) {
		is Float ->
			when {
				this > number.toFloat() -> 1
				this == number.toFloat() -> 0
				else -> -1
			}

		is Double ->
			when {
				this > number.toDouble() -> 1
				this == number.toDouble() -> 0
				else -> -1
			}

		is Long ->
			when {
				this > number.toLong() -> 1
				this == number.toLong() -> 0
				else -> -1
			}

		is Int ->
			when {
				this > number.toInt() -> 1
				this == number.toInt() -> 0
				else -> -1
			}

		is Short ->
			when {
				this > number.toShort() -> 1
				this == number.toShort() -> 0
				else -> -1
			}

		else -> when {
			this > number.toDouble() -> 1
			this == number.toDouble() -> 0
			else -> -1
		}
	}
}

operator fun Number.div(number: Number): Number {
	return when (this) {
		is Float ->
			this / number.toFloat()

		is Double ->
			this / number.toDouble()

		is Long ->
			this / number.toLong()

		is Int ->
			this / number.toInt()

		is Short ->
			this / number.toShort()

		else -> (this.toDouble() / number.toDouble())
	}
}