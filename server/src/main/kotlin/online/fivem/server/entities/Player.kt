package online.fivem.server.entities

import online.fivem.server.gtav.Natives

class Player(
	val playerSrc: PlayerSrc,
	val name: String,
	val userId: Int,

	val sessionId: Int
) {
	var characterId: Int = -1

	override fun equals(other: Any?): Boolean {
		return other is Player && userId == other.userId
	}

	suspend fun drop(reason: String) {
		Natives.dropPlayer(playerSrc, reason)
	}
}