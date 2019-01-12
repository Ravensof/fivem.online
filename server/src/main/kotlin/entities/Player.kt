package online.fivem.server.entities

import online.fivem.common.entities.PlayerSrc

class Player(
	val playerSrc: PlayerSrc,
	val name: String,

	var sessionId: Int = -1,
	var characterId: Int = -1
)