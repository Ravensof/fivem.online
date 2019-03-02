package online.fivem.server.entities

import online.fivem.common.entities.PlayerSrc
import online.fivem.server.gtav.Natives

class Player(
	val playerSrc: PlayerSrc,
	val name: String,
	val userId: Int,

	val sessionId: Int
) {
	var characterId: Int = -1

	fun drop(reason: String) {
		Natives.dropPlayer(playerSrc, reason)
	}
}