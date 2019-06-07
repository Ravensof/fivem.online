package online.fivem.client.common

import online.fivem.client.entities.Ped
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Client.getPlayerInvincible
import online.fivem.client.gtav.Client.requestModel
import online.fivem.client.gtav.Client.setModelAsNoLongerNeeded
import online.fivem.client.gtav.Client.setPlayerInvincible
import online.fivem.client.gtav.Client.setPlayerModel

class Player(
	val id: Int
) {
	var ped = Ped.newInstance(Client.getPlayerPedId())
		private set

	var isInvincible: Boolean
		get() = getPlayerInvincible(id)
		set(value) = setPlayerInvincible(id, value)

	fun clearWantedLevel() = Client.clearPlayerWantedLevel(id)

	suspend fun setModel(hash: Int) = try {
		requestModel(hash)
		setPlayerModel(id, hash)
		ped = Ped.newInstance(Client.getPlayerPedId())
	} finally {
		setModelAsNoLongerNeeded(hash)
	}

}