package online.fivem.client.common

import online.fivem.client.entities.Ped
import online.fivem.client.gtav.Client
import online.fivem.client.gtav.Client.getPlayerInvincible
import online.fivem.client.gtav.Client.setPlayerInvincible
import online.fivem.client.gtav.Client.setPlayerModel

class Player(
	val id: Int
) {
	var ped = getPed()
		private set

	var isInvincible: Boolean
		get() = getPlayerInvincible(id)
		set(value) = setPlayerInvincible(id, value)

	fun clearWantedLevel() = Client.clearPlayerWantedLevel(id)

	suspend fun setModel(hash: Int) {
		setPlayerModel(id, hash)
		ped = getPed()
	}

	fun networkGetLoudness() = Client.networkGetPlayerLoudness(id)

	private fun getPed() =
		Ped.newInstance(
			Client.getPlayerPed(id) ?: throw IllegalStateException("player's ped can't be null")
		)
}