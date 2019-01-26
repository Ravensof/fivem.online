package online.fivem.common.entities

class PlayerSrc(
	val value: Int
) {
	override fun equals(other: Any?): Boolean {
		return other is PlayerSrc && other.value == value
	}

	override fun hashCode(): Int {
		return value
	}
}