package online.fivem.server.entities

class PlayerSrc(
	val value: Int
) {
	init {
		if (value <= 0) throw IllegalArgumentException("playerSrc have te be 1 or higher")
	}

	override fun equals(other: Any?): Boolean {
		return other is PlayerSrc && other.value == value
	}

	override fun hashCode(): Int {
		return value.hashCode()
	}
}