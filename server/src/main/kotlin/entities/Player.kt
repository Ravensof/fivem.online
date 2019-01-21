package online.fivem.server.entities

import online.fivem.common.entities.PlayerSrc

class Player(
	val playerSrc: PlayerSrc,
	val name: String,

	val sessionId: Int,
	var characterId: Int = -1
)